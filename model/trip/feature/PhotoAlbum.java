package model.trip.feature;

import java.util.ArrayList;

public class PhotoAlbum implements Feature
{
	private ArrayList<String> imagePaths = new ArrayList<String>();
	
	public PhotoAlbum(ArrayList<String> toImages)
	{
		imagePaths=toImages;
	}
	
	public ArrayList<String> getImagePaths()
	{
		return imagePaths;
	}
	
	public void setImages(ArrayList<String> newImages)
	{
		imagePaths=newImages;
	}
	
	public void addImage(String newImagesPath)
	{
		imagePaths.add(newImagesPath);
	}
	
	public void deleteImage(String badImage)
	{
		imagePaths.remove(badImage);
	}
}
