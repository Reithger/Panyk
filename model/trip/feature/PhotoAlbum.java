package model.trip.feature;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements the Feature interface for consistency in functionality
 * with other Feature-implementing classes used in a Trip object to model a
 * PhotoAlbum; that is to say, a means by which a User can view the photos they
 * have associated to this Trip and provide comments to.
 * 
 * @author Reithger
 *
 */

public class PhotoAlbum implements Feature{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private ArrayList<String> imagePaths = new ArrayList<String>();
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param toImages
	 */
	
	public PhotoAlbum(ArrayList<String> toImages){
		imagePaths = toImages;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 * @param newImagesPath
	 */
	
	public void addImage(String newImagesPath)
	{
		imagePaths.add(newImagesPath);
	}
	
	/**
	 * 
	 * @param badImage
	 */
	
	public void deleteImage(String badImage)
	{
		imagePaths.remove(badImage);
	}

	@Override
	public HashMap<String, String> exportDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportMemory() {
		// TODO Auto-generated method stub
		
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @return
	 */
	
	public ArrayList<String> getImagePaths()
	{
		return imagePaths;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @param newImages
	 */
	
	public void setImages(ArrayList<String> newImages)
	{
		imagePaths = newImages;
	}


}
