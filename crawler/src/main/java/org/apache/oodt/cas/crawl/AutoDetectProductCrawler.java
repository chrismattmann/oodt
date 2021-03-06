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


package org.apache.oodt.cas.crawl;

//OODT imports
import org.apache.oodt.cas.crawl.typedetection.MetExtractorSpec;
import org.apache.oodt.cas.crawl.typedetection.MimeExtractorConfigReader;
import org.apache.oodt.cas.crawl.typedetection.MimeExtractorRepo;
import org.apache.oodt.cas.filemgr.metadata.CoreMetKeys;
import org.apache.oodt.cas.metadata.MetExtractor;
import org.apache.oodt.cas.metadata.Metadata;
import org.apache.oodt.cas.metadata.preconditions.PreCondEvalUtils;

//JDK imports
import java.io.File;
import java.util.List;
import java.util.logging.Level;

//APACHE imports
import org.apache.tika.mime.MimeType; //for javadoc

//Spring imports
import org.springframework.beans.factory.annotation.Required;

/**
 * @author mattmann
 * @author bfoster
 * @version $Revision$
 * 
 * <p>
 * A {@link ProductCrawler} that uses a suite of files to define its crawling
 * and ingestion policy:
 * 
 * <ul>
 * <li><code>actions-map.xml</code> - This file is an XML specification for
 * actions that the crawler should take in response to its 3 lifecycle phases:
 * preIngest, postIngestSuccess, and postIngestFail. </li>
 * <li><code>met-extr-preconditions.xml</code> - This file defines
 * preconditions that {@link MetExtractor}s must pass before being called by
 * the AutoDetectCrawler. </li>
 * <li><code>mime-extractor-map.xml</code> - This file maps {@link MimeType}
 * names to names of {@link MetExtractor}s to call for a particular
 * {@link Product} {@link File} as it is encountered during a crawl (e.g.,
 * assuming that {@link Metadata} needs to be generated, as oppossed to being
 * available apriori). See
 * <code>./src/resources/examples/mime-extractor-map.xml</code> for an example
 * of the structure of this file. </li>
 * <li><code>mimetypes.xml</code> - An <a
 * href="http://incubator.apache.org/tika/">Apache Tika</a> style mimetypes
 * file, augmented with the ability to have arbitrary regular expressions that
 * define a particular {@link Product} {@link MimeType}. This {@link MimeType}
 * is then mapped to an extractor vai the <code>mime-extractor-map.xml</code>
 * file, described above. </li>
 * </p>.
 */
public class AutoDetectProductCrawler extends ProductCrawler implements
        CoreMetKeys {
    
    private MimeExtractorRepo mimeExtractorRepo;
    
    protected Metadata getMetadataForProduct(File product) {
        try {
            List<MetExtractorSpec> specs = this.mimeExtractorRepo
                    .getExtractorSpecsForFile(product);
            Metadata metadata = new Metadata();
            metadata.addMetadata(MIME_TYPES_HIERARCHY, 
                    this.mimeExtractorRepo.getMimeTypes(product));
            for (int i = 0; i < specs.size(); i++) {
                Metadata m = ((MetExtractorSpec) specs.get(i))
                        .getMetExtractor().extractMetadata(product);
                if (m != null)
                    metadata.addMetadata(m.getHashtable(), true);
            }
            return metadata;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Failed to get metadata for product "
                    + product + " : " + e.getMessage());
            return new Metadata();
        }
    }

    protected boolean passesPreconditions(File product) {
        try {
            List<MetExtractorSpec> specs = this.mimeExtractorRepo
                    .getExtractorSpecsForFile(product);
            if (specs.size() > 0) {
                if (this.getApplicationContext() != null) {
                    PreCondEvalUtils evalUtils = new PreCondEvalUtils(
                            this.getApplicationContext());
                    for (int i = 0; i < specs.size(); i++) {
                        List<String> preCondComparatorIds = ((MetExtractorSpec) specs
                                .get(i)).getPreCondComparatorIds();
                        if (!evalUtils.eval(preCondComparatorIds, product))
                            return false;
                    }
                }
                return true;
            } else {
                LOG.log(Level.WARNING, "No extractor specs specified for "
                        + product);
                return false;
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Failed to evaluate preconditions : "
                    + e.getMessage());
            return false;
        }
    }

    @Required
    public void setMimeExtractorRepo(String mimeExtractorRepo) throws Exception {
        this.mimeExtractorRepo = MimeExtractorConfigReader
                .read(mimeExtractorRepo);
    }

}
