/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.oodt.cas.crawl.typedetection;

//JDK imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

//OODT imports
import org.apache.oodt.cas.metadata.util.MimeTypeUtils;

/**
 * @author mattmann
 * @author bfoster
 * @version $Revision$
 */
public class MimeExtractorRepo {

	private List<MetExtractorSpec> defaultExtractorSpecs;

	private MimeTypeUtils mimeRepo;

	private boolean magic;

	private HashMap<String, List<MetExtractorSpec>> mimeTypeToMetExtractorSpecsMap;

	/**
	 * Default Constructor
	 * 
	 * @throws FileNotFoundException
	 * 
	 */
	public MimeExtractorRepo() throws FileNotFoundException {
		this(new LinkedList<MetExtractorSpec>(), null, false);
	}

	/**
	 * Constructs a new MimeExtractorMappingFile with the given parameters.
	 * 
	 * @param defaultExtractorClassName
	 *            The name of the default extractor to call if the mime type
	 *            can't be determined.
	 * @param repo
	 *            The Mime Repository to use for mime resolution.
	 * @param magic
	 *            Whether or not mime magic should be used or not in resolution.
	 * 
	 * @param mappings
	 *            {@link List} of {@link MimeExtractorMapping}s.
	 * @throws FileNotFoundException
	 */
	public MimeExtractorRepo(List<MetExtractorSpec> defaultExtractorSpecs,
			String mimeRepoFile, boolean magic) throws FileNotFoundException {
		this.setDefaultMetExtractorSpecs(defaultExtractorSpecs);
		this.setMimeRepoFile(mimeRepoFile);
		this.setMagic(magic);
		this.mimeTypeToMetExtractorSpecsMap = new HashMap<String, List<MetExtractorSpec>>();
	}

	public synchronized void addMetExtractorSpec(String mimeType,
			MetExtractorSpec spec) {
		List<MetExtractorSpec> specs = this.mimeTypeToMetExtractorSpecsMap
				.remove(mimeType);
		if (specs == null)
			specs = new LinkedList<MetExtractorSpec>();
		specs.add(spec);
		this.mimeTypeToMetExtractorSpecsMap.put(mimeType, specs);
	}

	public synchronized void addMetExtractorSpecs(String mimeType,
			List<MetExtractorSpec> specs) {
		List<MetExtractorSpec> existingSpecs = this.mimeTypeToMetExtractorSpecsMap
				.remove(mimeType);
		if (existingSpecs == null)
			existingSpecs = new LinkedList<MetExtractorSpec>();
		existingSpecs.addAll(specs);
		this.mimeTypeToMetExtractorSpecsMap.put(mimeType, existingSpecs);
	}

	public synchronized List<MetExtractorSpec> getExtractorSpecsForMimeType(
			String mimeType) {
		List<MetExtractorSpec> extractorSpecs = new LinkedList<MetExtractorSpec>();
		while (mimeType != null && !mimeType.equals("application/octet-stream")) {
			List<MetExtractorSpec> specs = this.mimeTypeToMetExtractorSpecsMap.get(mimeType);
			if (specs != null)
				extractorSpecs.addAll(specs);
			mimeType = this.mimeRepo.getSuperTypeForMimeType(mimeType);
		}
		return extractorSpecs != null ? extractorSpecs : this
				.getDefaultMetExtractorSpecs();
	}

	public synchronized List<MetExtractorSpec> getExtractorSpecsForFile(
			File file) throws FileNotFoundException, IOException {
		String mimeType = this.mimeRepo.getMimeType(file);
		if (mimeType == null && magic)
			mimeType = this.mimeRepo.getMimeTypeByMagic(MimeTypeUtils
					.readMagicHeader(new FileInputStream(file)));
		return this.getExtractorSpecsForMimeType(mimeType);
	}

	/**
	 * @return the defaultExtractorClassName
	 */
	public List<MetExtractorSpec> getDefaultMetExtractorSpecs() {
		return this.defaultExtractorSpecs != null ? this.defaultExtractorSpecs
				: new LinkedList<MetExtractorSpec>();
	}

	/**
	 * @param defaultExtractorClassName
	 *            the defaultExtractorClassName to set
	 */
	public void setDefaultMetExtractorSpecs(
			List<MetExtractorSpec> defaultExtractorSpecs) {
		this.defaultExtractorSpecs = defaultExtractorSpecs;
	}

	/**
	 * @return the magic
	 */
	public boolean isMagic() {
		return this.magic;
	}

	/**
	 * @param magic
	 *            the magic to set
	 */
	public void setMagic(boolean magic) {
		this.magic = magic;
		if (this.mimeRepo != null)
			this.mimeRepo.setMimeMagic(magic);
	}

	/**
	 * @param mimeRepo
	 *            the mimeRepo to set
	 * @throws FileNotFoundException
	 */
	public void setMimeRepoFile(String mimeRepoFile)
			throws FileNotFoundException {
		if (mimeRepoFile != null)
			this.mimeRepo = new MimeTypeUtils(mimeRepoFile, this.magic);
	}
	
	/**
	 * Gets the mime-type hierarchy. Index 0 is this files mime-type,
	 * index 1 is index 0's mime-type's parent mime-type, and so on. 
	 * @param file
	 * @return
	 */
	public List<String> getMimeTypes(File file) {
	    List<String> mimeTypes = new Vector<String>();
	    String mimeType = this.mimeRepo.getMimeType(file);
	    mimeTypes.add(mimeType);
	    while ((mimeType = this.mimeRepo.getSuperTypeForMimeType(mimeType)) != null
                && !mimeType.equals("application/octet-stream"))
	        mimeTypes.add(mimeType);
	    return mimeTypes;
	}

}
