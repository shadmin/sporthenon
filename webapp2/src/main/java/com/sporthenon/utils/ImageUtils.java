package com.sporthenon.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;
import com.sporthenon.db.DatabaseManager;
import com.sporthenon.db.entity.meta.Picture;
import com.sporthenon.utils.res.ResourceUtils;

@SuppressWarnings("unchecked")
public class ImageUtils {

	private static final Logger log = Logger.getLogger(ImageUtils.class.getName());
	
	public static final short INDEX_SPORT 		 		= 0;
	public static final short INDEX_CHAMPIONSHIP 		= 1;
	public static final short INDEX_STATE 		 		= 2;
	public static final short INDEX_OLYMPICS 	 		= 3;
	public static final short INDEX_COUNTRY 	 		= 4;
	public static final short INDEX_TEAM 		 		= 5;
	public static final short INDEX_EVENT 		 		= 6;
	public static final short INDEX_SPORT_CHAMPIONSHIP 	= 7;
	public static final short INDEX_SPORT_EVENT  		= 8;
	
	public static final char SIZE_LARGE = 'L';
	public static final char SIZE_SMALL = 'S';
	
	private static Map<String, Short> mapIndex;
	private static Map<String, List<String>> mapFiles;
	private static String imgURL = null;
	private static String bucket = null;
	private static String folder = null;
	
	static {
		mapIndex = new HashMap<String, Short>();
		mapIndex.put("SP", INDEX_SPORT);
		mapIndex.put("CP", INDEX_CHAMPIONSHIP);
		mapIndex.put("ST", INDEX_STATE);
		mapIndex.put("OL", INDEX_OLYMPICS);
		mapIndex.put("CN", INDEX_COUNTRY);
		mapIndex.put("TM", INDEX_TEAM);
		mapIndex.put("EV", INDEX_EVENT);
		mapIndex.put("SPCP", INDEX_SPORT_CHAMPIONSHIP);
		mapIndex.put("SPEV", INDEX_SPORT_EVENT);
	}

	public static short getIndex(String alias) {
		return mapIndex.containsKey(alias) ? mapIndex.get(alias) : -1;
	}
	
	public static String getUrl() {
		if (imgURL == null) {
			imgURL = String.format("https://storage.googleapis.com/%s/%s", getBucket(), getFolder()); 
		}
		return imgURL;
	}
	
	private static String getBucket()  {
		if (bucket == null) {
			bucket = ConfigUtils.getProperty("bucket.name");
		}
		return bucket;
	}
	
	private static String getFolder()  {
		if (folder == null) {
			folder = ConfigUtils.getProperty("bucket.folder") + "/";
		}
		return folder;
	}
	
	private static void initFileMap() {
		try {
			mapFiles = new HashMap<>();
			Storage storage = StorageOptions.getDefaultInstance().getService();
		    String bucket = getBucket();
		    String folder = getFolder();
		    Page<Blob> files = storage.list(bucket, BlobListOption.currentDirectory(), BlobListOption.prefix(folder));
		    Iterator<Blob> it = files.iterateAll().iterator();
		    while (it.hasNext()) {
		      Blob file = it.next();
		      String fileName = file.getName().replace(folder, "");
		      String key = fileName.replaceFirst("(\\.|\\_).+", "");
		      addImageToMap(key, fileName);
		    }
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	public static void addImageToMap(String key, String fileName) {
		List<String> value = mapFiles.containsKey(key) ? mapFiles.get(key) : new ArrayList<>();
		value.add(fileName);
		mapFiles.put(key, value);
	}
	
	public static void removeImageFromMap(String fileName) {
		String key = fileName.replaceFirst("(\\.|\\_).+", "");
		if (mapFiles.containsKey(key)) {
			mapFiles.get(key).remove(fileName);
		}
	}
	
	public static void uploadImage(String key, String fileName, byte[] content, String credFile) throws IOException {
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credFile));
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		BlobId blobId = BlobId.of(getBucket(), getFolder() + fileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();
		storage.create(blobInfo, content);
		addImageToMap(key, fileName);
	}
	
	public static void removeImage(String fileName, String credFile) throws IOException {
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credFile));
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		BlobId blobId = BlobId.of(getBucket(), getFolder() + fileName);
		storage.delete(blobId);
		removeImageFromMap(fileName);
	}
	
	public static Collection<String> getImages(String key) {
		if (mapFiles == null) {
			initFileMap();
		}
		return mapFiles.containsKey(key) ? mapFiles.get(key) : Collections.EMPTY_LIST;
	}
	
	public static Collection<String> getImages(short type, Object id, char size) {
		String key = type + "-" + id + "-" + size;
		return getImages(key);
	}

	public static StringBuffer getPhotos(String entity, Object id, String lang) throws Exception {
		final int MAX_WIDTH = Integer.parseInt(ConfigUtils.getValue("max_photo_width"));
		final int MAX_HEIGHT = Integer.parseInt(ConfigUtils.getValue("max_photo_height"));
		StringBuffer sb = new StringBuffer();
		for (Picture p : (List<Picture>) DatabaseManager.executeSelect("SELECT * FROM _picture WHERE entity='" + entity + "' AND id_item=" + id + " ORDER BY id", Picture.class)) {
			sb.append("<li id='ph-" + p.getId() + "'>");
			if (p.isEmbedded()) {
				String value = p.getValue().replaceAll("%", "px");
				value = value.replaceAll("height\\:100px\\;", "");
				value = value.replaceAll("width\\:100px\\;", "width:" + MAX_WIDTH + "px;height:" + MAX_HEIGHT + "px;");
				value = value.replaceFirst("padding:[\\d\\.]+px", "padding:0");
				sb.append(value);
			}
			else {
				sb.append("<img alt='" + (StringUtils.notEmpty(p.getSource()) ? p.getSource() : "") + "' src='" + ImageUtils.getUrl() + p.getValue() + "'/>");
			}
			sb.append("<div class='enlarge'><a href='javascript:enlargePhoto(" + p.getId() + ");'><img src='" + getRenderUrl() + "enlarge.png' title='" + ResourceUtils.getText("enlarge", lang) + "'/></a></div></li>");
		}
		return (sb.length() > 0 ? sb : null);
	}
	
	public static String getRenderUrl() {
		return "/img/render/";
	}
	
	public static String getGoldMedImg(String lang) {
		return "<img alt='Gold' title='" + ResourceUtils.getText("gold", lang) + "' src='" + getRenderUrl() + "gold.png?4'/>";
	}
	
	public static String getSilverMedImg(String lang) {
		return "<img alt='Silver' title='" + ResourceUtils.getText("silver", lang) + "' src='" + getRenderUrl() + "silver.png?4'/>";
	}
	
	public static String getBronzeMedImg(String lang) {
		return "<img alt='Bronze' title='" + ResourceUtils.getText("bronze", lang) + "' src='" + getRenderUrl() + "bronze.png?4'/>";
	}
	
	public static String getGoldHeader(String lang) {
		return "<table><tr><td><img alt='" + ResourceUtils.getText("gold", lang) + "' src='" + getRenderUrl() + "gold.png'/></td><td class='bold'>" + ResourceUtils.getText("gold", lang) + "</td></tr></table>";
	}
	
	public static String getSilverHeader(String lang) {
		return "<table><tr><td><img alt='" + ResourceUtils.getText("silver", lang) + "' src='" + getRenderUrl() + "silver.png'/></td><td class='bold'>" + ResourceUtils.getText("silver", lang) + "</td></tr></table>";
	}
	
	public static String getBronzeHeader(String lang) {
		return "<table><tr><td><img alt='" + ResourceUtils.getText("bronze", lang) + "' src='" + getRenderUrl() + "bronze.png'/></td><td class='bold'>" + ResourceUtils.getText("bronze", lang) + "</td></tr></table>";
	}
	
}