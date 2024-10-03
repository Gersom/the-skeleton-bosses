/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mu.gersom.generators;

import org.bukkit.Location;
import org.bukkit.World;

import mu.gersom.MuMc;

/**
 *
 * @author Gersom
 */
public class MainGenerator {
    private SkeletonEmperor skeletonEmperor;
    private SkeletonKing skeletonKing;
    
    public void generateEmperor(MuMc plugin, World world, Location location) {
        skeletonEmperor = new SkeletonEmperor(plugin);
        skeletonEmperor.generateSkeletonEmperor(world, location);
    }

    public void generateKing(MuMc plugin, World world, Location location) {
        skeletonKing = new SkeletonKing(plugin);
        skeletonKing.generateSkeletonKing(world, location);
    }

    public SkeletonEmperor getSkeletonEmperor() {
        return skeletonEmperor;
    }

    public SkeletonKing getSkeletonKing() {
        return skeletonKing;
    }
}
