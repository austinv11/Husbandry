package com.austinv11.husbandry.api.genetics;

import com.austinv11.husbandry.api.HusbandryAPI;
import com.austinv11.husbandry.api.IHusbandryRegistry;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is a holder object, representing all the traits from an animal
 */
public class Traits {
	
	private static Random rng = new Random();
	
	/**
	 * The genes received from the father
	 */
	public final List<Gene> fatherGenes;
	/**
	 * The genes received from the mother
	 */
	public final List<Gene> motherGenes;
	/**
	 * The gender of the animal
	 */
	public Gender gender = Gender.UNKNOWN;
	
	private List<Gene> activeGenes = new ArrayList<Gene>();
	
	/**
	 * Constructor for use to generate traits for an offspring
	 * @param father The father
	 * @param mother The mother
	 * @param child The child
	 */
	public Traits(EntityLiving father, EntityLiving mother, EntityLiving child) {
		this.fatherGenes = new ArrayList<Gene>();
		this.motherGenes = new ArrayList<Gene>();
		this.gender = rng.nextBoolean() ? Gender.MALE : Gender.FEMALE;
		Traits fatherTraits = new Traits(father);
		Traits motherTraits = new Traits(mother);
		try {
			fatherGenes.addAll(getInheritedGenes(fatherTraits, child, father, mother));
			motherGenes.addAll(getInheritedGenes(motherTraits, child, father, mother));
		} catch (Exception e) {
			FMLLog.log("HusbandryAPI", Level.ERROR, "Error generating traits for "+child.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor for use to extract the traits information from an animal
	 * @param animal The animal
	 */
	public Traits(EntityLiving animal) {
		this.fatherGenes = new ArrayList<Gene>();
		this.motherGenes = new ArrayList<Gene>();
		try {
			NBTTagCompound traitsTag = animal.getEntityData().getCompoundTag("genes");
			gender = Gender.valueOf(traitsTag.getString("gender"));
			NBTTagList fatherIds = traitsTag.getTagList("fatherIds", Constants.NBT.TAG_STRING);
			NBTTagCompound fatherTag = traitsTag.getCompoundTag("father");
			for (int i = 0; i < fatherIds.tagCount(); i++) {
				String id = fatherIds.getStringTagAt(i);
				NBTTagCompound geneTag = fatherTag.getCompoundTag(id);
				Gene toConstructFrom = HusbandryAPI.retrieveRegistry().getGenes().get(id);
				Constructor constructor = toConstructFrom.getClass().getConstructor(EntityLiving.class, NBTTagCompound.class);
				fatherGenes.add((Gene) constructor.newInstance(animal, geneTag));
			}
			NBTTagList motherIds = traitsTag.getTagList("motherIds", Constants.NBT.TAG_STRING);
			NBTTagCompound motherTag = traitsTag.getCompoundTag("mother");
			for (int i = 0; i < motherIds.tagCount(); i++) {
				String id = motherIds.getStringTagAt(i);
				NBTTagCompound geneTag = motherTag.getCompoundTag(id);
				Gene toConstructFrom = HusbandryAPI.retrieveRegistry().getGenes().get(id);
				Constructor constructor = toConstructFrom.getClass().getConstructor(EntityLiving.class, NBTTagCompound.class);
				motherGenes.add((Gene) constructor.newInstance(animal, geneTag));
			}
		} catch (Exception e) {
			FMLLog.log("HusbandryAPI", Level.ERROR, "Error loading traits from NBT from entity "+animal.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor for use to generate custom traits
	 * @param fatherGenes The genes from the father
	 * @param motherGenes The genes from the mother
	 * @param gender The gender for the traits
	 */
	public Traits(List<Gene> fatherGenes, List<Gene> motherGenes, Gender gender) {
		this.fatherGenes = fatherGenes;
		this.motherGenes = motherGenes;
		this.gender = gender;
	}
	
	/**
	 * Writes the stored traits into an animal's NBT data
	 * @param animal The animal to store the traits into
	 */
	public void writeToAnimal(EntityLiving animal) {
		NBTTagCompound traitsTag = writeToTag();
		animal.getEntityData().setTag("genes", traitsTag);
	}
	
	/**
	 * Writes the traits instance to an {@link NBTTagCompound}
	 * @return The traits tag
	 */
	public NBTTagCompound writeToTag() {
		NBTTagCompound traitsTag = new NBTTagCompound();
		NBTTagCompound fatherTag = new NBTTagCompound();
		NBTTagList fatherIds = new NBTTagList();
		for (Gene fatherGene : fatherGenes) {
			fatherTag.setTag(fatherGene.getUnlocalizedName(), fatherGene.toNBT());
			fatherIds.appendTag(new NBTTagString(fatherGene.getUnlocalizedName()));
		}
		traitsTag.setTag("father", fatherTag);
		traitsTag.setTag("fatherIds", fatherIds);
		NBTTagCompound motherTag = new NBTTagCompound();
		NBTTagList motherIds = new NBTTagList();
		for (Gene motherGene : motherGenes) {
			motherTag.setTag(motherGene.getUnlocalizedName(), motherGene.toNBT());
			motherIds.appendTag(new NBTTagString(motherGene.getUnlocalizedName()));
		}
		traitsTag.setTag("mother", motherTag);
		traitsTag.setTag("motherIds", motherIds);
		traitsTag.setString("gender", gender.toString());
		return traitsTag;
	}
	
	/**
	 * Searches for the presence of a given gene in the provided list
	 * @param toSearch The list to search for genes from
	 * @param toSearchFor The gene to look for
	 * @return The index of the gene found, or -1 if not found
	 */
	public static int searchForGene(List<Gene> toSearch, Gene toSearchFor) {
		for (int i = 0; i < toSearch.size(); i++)
			if (toSearchFor.getUnlocalizedName().equals(toSearch.get(i).getUnlocalizedName()))
				return i;
		return -1;
	}
	
	/**
	 * Gets the genes that have an active effect
	 * @return The active genes
	 */
	public List<Gene> getActiveGenes() {
		if (activeGenes.isEmpty()) {
			for (Gene gene : fatherGenes) {
				if (gene.isRecessive()) {
					if (searchForGene(motherGenes, gene) != -1 && gene.isActive())
						activeGenes.add(gene);
					continue;
				}
				if (gene.isActive())
					activeGenes.add(gene);
			}
			for (Gene gene : motherGenes) {
				if (gene.isRecessive()) {
					if (searchForGene(fatherGenes, gene) != -1 && gene.isActive())
						activeGenes.add(gene);
					continue;
				}
				if (gene.isActive())
					activeGenes.add(gene);
			}
		}
		return new ArrayList<Gene>(activeGenes);
	}
	
	/**
	 * Checks if the traits for an animal has been initialized yet
	 * @param animal The animal
	 * @return True if traits have already been initialized, false if otherwise
	 */
	public static boolean hasTraits(EntityLiving animal) {
		return animal.getEntityData().hasKey("genes");
	}
	
	/**
	 * Randomly generates traits with {@link NaturalGene}s only
	 * @param animal The animal to generate the traits for
	 * @return The traits
	 */
	public static Traits generateTraits(EntityLiving animal) {
		try {
			List<NaturalGene> applicableGenes = findApplicableGenes(animal);
			List<Gene> fatherGenes = new ArrayList<Gene>();
			List<Gene> motherGenes = new ArrayList<Gene>();
			for (NaturalGene gene : applicableGenes) {
				if (rng.nextFloat() <= gene.getSpawnChance())
					motherGenes.add(gene);
				if (rng.nextFloat() <= gene.getSpawnChance())
					fatherGenes.add(gene);
			}
			return new Traits(fatherGenes, motherGenes, rng.nextBoolean() ? Gender.MALE : Gender.FEMALE);
		} catch (Exception e) {
			FMLLog.log("HusbandryAPI", Level.ERROR, "Error generating traits for "+animal.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	private static List<NaturalGene> findApplicableGenes(EntityLiving animal) throws Exception {
		List<NaturalGene> applicableGenes = new ArrayList<NaturalGene>();
		List<Gene> allGenes = new ArrayList<Gene>(HusbandryAPI.retrieveRegistry().getGenes().values());
		for (Gene gene : allGenes) {
			if (gene instanceof NaturalGene) {
				if (((NaturalGene) gene).canSpawn(animal))
					applicableGenes.add((NaturalGene) gene);
			}
		}
		return applicableGenes;
	}
	
	private static List<Gene> getInheritedGenes(Traits origin, EntityLiving child, EntityLiving father, EntityLiving mother) throws Exception {
		List<Gene> inherited = new ArrayList<Gene>();
		IHusbandryRegistry registry = HusbandryAPI.retrieveRegistry();
		for (Gene gene : origin.fatherGenes) {
			if (gene instanceof MutatedGene) {
				if (rng.nextFloat() <= ((MutatedGene) gene).getTransmissionChance()) {
					if (((MutatedGene)registry.getGenes().get(gene.getUnlocalizedName())).canTransmit(child)) {
						inherited.add(generateGene(gene.getClass(), child, father, mother));
					}
				}
			}
		}
		for (Gene gene : origin.motherGenes) {
			if (gene instanceof MutatedGene) {
				if (rng.nextFloat() <= ((MutatedGene) gene).getTransmissionChance()) {
					if (((MutatedGene)registry.getGenes().get(gene.getUnlocalizedName())).canTransmit(child)) {
						inherited.add(generateGene(gene.getClass(), child, father, mother));
					}
				}
			}
		}
		return cleanRepeats(inherited);
	}
	
	private static Gene generateGene(Class<? extends Gene> fromGene, EntityLiving animal, EntityLiving father, EntityLiving mother) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Constructor geneConstructor = fromGene.getConstructor(EntityLiving.class, EntityLiving.class, EntityLiving.class);
		return (Gene) geneConstructor.newInstance(animal, father, mother);
	}
	
	private static List<Gene> cleanRepeats(List<Gene> toClean) {
		List<String> storedGenes = new ArrayList<String>();
		List<Gene> cleanedGenes = new ArrayList<Gene>();
		for (Gene gene : toClean) {
			if (!storedGenes.contains(gene.getUnlocalizedName())) {
				storedGenes.add(gene.getUnlocalizedName());
				cleanedGenes.add(gene);
			}
		}
		return cleanedGenes;
	}
}
