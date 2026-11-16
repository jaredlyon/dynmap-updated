package org.dynmap.hdmap.renderer;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Map;

import org.dynmap.renderer.CustomRenderer;
import org.dynmap.renderer.MapDataContext;
import org.dynmap.renderer.RenderPatch;
import org.dynmap.renderer.RenderPatchFactory;
import org.dynmap.renderer.RenderPatchFactory.SideVisible;

public class WallHeadRenderer extends CustomRenderer {
    private static final int[] ROTATIONS = { 0, 180, 270, 90 }; // north, south, west, east
    
    private RenderPatch basemesh[];
    private final RenderPatch meshes[][] = new RenderPatch[ROTATIONS.length][];
    
    @Override
    public boolean initializeRenderer(RenderPatchFactory rpf, String blkname, BitSet blockdatamask, Map<String,String> custparm) {
        if(!super.initializeRenderer(rpf, blkname, blockdatamask, custparm))
            return false;

        ArrayList<RenderPatch> list = new ArrayList<>();
        list.add(rpf.getPatch(0.75, 0.25, 0.5, 0.25, 0.25, 0.5, 0.75, 0.25, 1.0, 0, 1, 0, 1, SideVisible.TOP, 0));
        list.add(rpf.getPatch(0.25, 0.75, 0.5, 0.75, 0.75, 0.5, 0.25, 0.75, 1.0, 0, 1, 0, 1, SideVisible.TOP, 0));
        RenderPatch side = rpf.getPatch(0.75, 0.25, 0.5, 0.25, 0.25, 0.5, 0.75, 0.75, 0.5, 0, 1, 0, 1, SideVisible.TOP, 0);
        RenderPatch side2 = rpf.getRotatedPatch(side, 0, 90, 0, 0.5, 0.5, 0.75, 0);
        RenderPatch side3 = rpf.getRotatedPatch(side, 0, 180, 0, 0.5, 0.5, 0.75, 0);
        RenderPatch side4 = rpf.getRotatedPatch(side, 0, 270, 0, 0.5, 0.5, 0.75, 0);
        list.add(side4);
        list.add(side);
        list.add(side2);
        list.add(side3);
        basemesh = list.toArray(new RenderPatch[0]);
        return true;
    }

    @Override
    public int getMaximumTextureCount() {
        return 6;
    }

    private static final int faces[] = { 0, 1, 2, 3, 4, 5 };
    
    @Override
    public RenderPatch[] getRenderPatchList(MapDataContext ctx) {
        // facing is north/south/west/east, with powered=true/false for each.
        int idx = (ctx.getBlockType().stateIndex >> 1) & (ROTATIONS.length - 1);
        if(meshes[idx] == null) {
            RenderPatchFactory rpf = ctx.getPatchFactory();
            RenderPatch[] rp = new RenderPatch[basemesh.length];
            for(int i = 0; i < rp.length; i++) {
                rp[i] = rpf.getRotatedPatch(basemesh[i], 0, ROTATIONS[idx], 0, faces[i]);
            }
            meshes[idx] = rp;
        }
        return meshes[idx];
    }
    @Override
    public boolean isOnlyBlockStateSensitive() {
    	return true;
    }
}
