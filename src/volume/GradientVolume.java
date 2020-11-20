/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volume;

/**
 *
 * @author michel
 */
public class GradientVolume {

    public GradientVolume(Volume vol) {
        volume = vol;
        dimX = vol.getDimX();
        dimY = vol.getDimY();
        dimZ = vol.getDimZ();
        data = new VoxelGradient[dimX * dimY * dimZ];
        compute();
        maxmag = -1.0;
    }

    public VoxelGradient getGradient(int x, int y, int z) {
        return data[x + dimX * (y + dimY * z)];
    }

    public void setGradient(int x, int y, int z, VoxelGradient value) {
        data[x + dimX * (y + dimY * z)] = value;
    }

    public void setVoxel(int i, VoxelGradient value) {
        data[i] = value;
    }

    public VoxelGradient getVoxel(int i) {
        return data[i];
    }

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    public int getDimZ() {
        return dimZ;
    }

    /**
     * Computes the gradient information of the volume according to Levoy's
     * paper.
     */
    private void compute() {
        // TODO 4: Implement gradient computation.
        // this just initializes all gradients to the vector (0,0,0)
        for (int i = 0; i < data.length; i++) {
            // basis conversion of 1D to 3D indexing
            int z = i/(dimX*dimY);
            int temp = i - (z*dimX*dimY);
            int y = temp/dimX;
            int x = temp % dimX;
            
            // gradient vector computed according to equation from slide 27 at 2-spatial.pdf
            // gradient = 0 at borders
            if (x < 1 || x > (dimX - 2) || y < 1 || y > (dimY - 2)
                || z < 1 || z > (dimZ - 2)) {
                
                data[i] = zero;
            }
            else{
                VoxelGradient G = new VoxelGradient((float) 1/2 * (volume.getVoxel(x+1,y,z) - volume.getVoxel(x-1,y,z)),
                (float) 1/2 * (volume.getVoxel(x+1,y,z) - volume.getVoxel(x-1,y,z)),
                (float) 1/2 * (volume.getVoxel(x,y,z+1) - volume.getVoxel(x,y,z-1)));
                
                //normalize gradient vector
                G.x = G.x/G.mag;
                G.y = G.y/G.mag;
                G.z = G.z/G.mag;
                G.mag = 1;
                data[i] = G;

            }


        }
     
        
    }

    public double getMaxGradientMagnitude() {
        if (maxmag >= 0) {
            return maxmag;
        } else {
            double magnitude = data[0].mag;
            for (int i = 0; i < data.length; i++) {
                magnitude = data[i].mag > magnitude ? data[i].mag : magnitude;
            }
            maxmag = magnitude;
            return magnitude;
        }
    }

    private int dimX, dimY, dimZ;
    private VoxelGradient zero = new VoxelGradient();
    VoxelGradient[] data;
    Volume volume;
    double maxmag;
}
