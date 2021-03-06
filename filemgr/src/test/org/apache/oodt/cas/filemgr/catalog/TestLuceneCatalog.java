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


package org.apache.oodt.cas.filemgr.catalog;

//JDK imports
import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;

//OODT imports
import org.apache.oodt.cas.filemgr.catalog.LuceneCatalog;
import org.apache.oodt.cas.filemgr.catalog.LuceneCatalogFactory;
import org.apache.oodt.cas.filemgr.metadata.CoreMetKeys;
import org.apache.oodt.cas.filemgr.structs.Product;
import org.apache.oodt.cas.filemgr.structs.ProductPage;
import org.apache.oodt.cas.filemgr.structs.ProductType;
import org.apache.oodt.cas.filemgr.structs.Reference;
import org.apache.oodt.cas.filemgr.structs.exceptions.CatalogException;
import org.apache.oodt.cas.metadata.Metadata;

//Junit imports
import junit.framework.TestCase;

/**
 * @author woollard
 * @author mattmann
 * 
 * @version $Revision$
 * 
 * <p>
 * Test suite for the {@link LuceneCatalog} and {@link LuceneCatalogFactory}.
 * </p>.
 */
public class TestLuceneCatalog extends TestCase {

    private LuceneCatalog myCat;

    private String tmpDirPath = null;

    private static final int catPageSize = 20;

    public TestLuceneCatalog() {
        // set the log levels
        System.setProperty("java.util.logging.config.file", new File(
                "./src/main/resources/logging.properties").getAbsolutePath());

        // first load the example configuration
        try {
            System.getProperties().load(
                    new FileInputStream("./src/main/resources/filemgr.properties"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // get a temp directory

        File tempDir = null;
        File tempFile = null;

        try {
            tempFile = File.createTempFile("foo", "bar");
            tempFile.deleteOnExit();
            tempDir = tempFile.getParentFile();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        tmpDirPath = tempDir.getAbsolutePath();
        if (!tmpDirPath.endsWith("/")) {
            tmpDirPath += "/";
        }

        tmpDirPath += "testCat/";

        // now override the catalog ones
        System.setProperty(
                "org.apache.oodt.cas.filemgr.catalog.lucene.idxPath",
                tmpDirPath);

        System.setProperty(
                "org.apache.oodt.cas.filemgr.catalog.lucene.pageSize", "20");

        System
                .setProperty(
                        "org.apache.oodt.cas.filemgr.catalog.lucene.commitLockTimeout.seconds",
                        "60");

        System
                .setProperty(
                        "org.apache.oodt.cas.filemgr.catalog.lucene.writeLockTimeout.seconds",
                        "60");

        System.setProperty(
                "org.apache.oodt.cas.filemgr.catalog.lucene.mergeFactor",
                "20");

        // now override the val layer ones
        System.setProperty("org.apache.oodt.cas.filemgr.validation.dirs",
                "file://"
                        + new File("./src/main/resources/examples")
                                .getAbsolutePath());

    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        myCat = (LuceneCatalog) new LuceneCatalogFactory().createCatalog();
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        // now remove the temporary directory used

        if (tmpDirPath != null) {
            File tmpDir = new File(tmpDirPath);
            File[] tmpFiles = tmpDir.listFiles();

            if (tmpFiles != null && tmpFiles.length > 0) {
                for (int i = 0; i < tmpFiles.length; i++) {
                    tmpFiles[i].delete();
                }

                tmpDir.delete();
            }

            if (myCat != null) {
                myCat = null;
            }
        }

    }

    public void testRemoveProduct() {
        Product productToRemove = getTestProduct();

        // override name
        productToRemove.setProductName("removeme");
        try {
            myCat.addProduct(productToRemove);
            myCat.addMetadata(getTestMetadata("tempProduct"), productToRemove);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        Product retProduct = null;
        ProductType type = new ProductType();
        type.setName("GenericFile");
        type.setProductTypeId("urn:oodt:GenericFile");

        try {
            retProduct = myCat.getProductByName("removeme");
            retProduct.setProductType(type);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertNotNull(retProduct);

        try {
            myCat.removeProduct(retProduct);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        Product retProdAfterRemove = null;
        try {
            retProdAfterRemove = myCat.getProductByName("removeme");
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNull(retProdAfterRemove);

    }

    public void testModifyProduct() {
        Product testProduct = getTestProduct();
        try {
            myCat.addProduct(testProduct);
            myCat.addMetadata(getTestMetadata("tempProduct"), testProduct);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(testProduct);
        assertEquals("test", testProduct.getProductName());
        // now change something
        testProduct.setProductName("f002");
        try {
            myCat.modifyProduct(testProduct);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertNotNull(testProduct);

        Product retProduct;
        try {
            retProduct = myCat.getProductByName("f002");
            assertNotNull(retProduct);
            assertEquals("f002", retProduct.getProductName());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    /**
     * @since OODT-133
     * 
     */
    public void testFirstProductOnlyOnFirstPage() {
        // add catPageSize of the test Product
        // then add a product called "ShouldBeFirstForPage.txt"
        // make sure it's the first one on the 2nd page

        Product testProd = getTestProduct();
        Metadata met = getTestMetadata("test");

        for (int i = 0; i < this.catPageSize; i++) {
            try {
                myCat.addProduct(testProd);
                myCat.addMetadata(met, testProd);
            } catch (Exception e) {
                fail(e.getMessage());
            }
        }

        testProd.setProductName("ShouldBeFirstForPage.txt");
        met.replaceMetadata("CAS.ProdutName", "ShouldBeFirstForPage.txt");

        try {
            myCat.addProduct(testProd);
            myCat.addMetadata(met, testProd);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            assertNotNull(myCat.getProducts());
            assertEquals(21, myCat.getProducts().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }

        ProductType type = new ProductType();
        type.setProductTypeId("urn:oodt:GenericFile");
        type.setName("GenericFile");
        assertNotNull(myCat.getFirstPage(type));
        assertNotNull(myCat.getFirstPage(type).getPageProducts());
        assertEquals(catPageSize, myCat.getFirstPage(type).getPageProducts()
                .size());
        ProductPage page = myCat.getNextPage(type, myCat.getFirstPage(type));
        assertNotNull(page);
        assertNotNull(page.getPageProducts());
        assertEquals(1, page.getPageProducts().size());
        assertEquals(2, page.getTotalPages());
        assertNotNull(page.getPageProducts().get(0));
        Product retProd = ((Product) page.getPageProducts().get(0));
        assertEquals("ShouldBeFirstForPage.txt", retProd.getProductName());
    }

    public void testAddProduct() {

        Product testProduct = getTestProduct();
        try {
            myCat.addProduct(testProduct);
            myCat.addMetadata(getTestMetadata("tempProduct"), testProduct);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        Product retProduct;
        try {
            retProduct = myCat.getProductByName("test");
            assertNotNull(retProduct);
            assertEquals("test", retProduct.getProductName());
            assertEquals(Product.STRUCTURE_FLAT, retProduct
                    .getProductStructure());
            assertNotNull(retProduct.getProductType());
            assertEquals("urn:oodt:GenericFile", retProduct.getProductType()
                    .getProductTypeId());
            assertEquals(Product.STATUS_TRANSFER, retProduct
                    .getTransferStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    public void testAddMetadata() {
        Metadata met = new Metadata();
        met.addMetadata("ProductStructure", Product.STRUCTURE_FLAT);

        Product testProduct = getTestProduct();
        try {
            myCat.addProduct(testProduct);
            myCat.addMetadata(met, testProduct);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            Metadata retMet = myCat.getMetadata(testProduct);
            assertNotNull(retMet);
            assertTrue(retMet.containsKey(CoreMetKeys.PRODUCT_STRUCTURE));
            assertEquals(Product.STRUCTURE_FLAT, retMet
                    .getMetadata(CoreMetKeys.PRODUCT_STRUCTURE));
        } catch (CatalogException e) {
            fail(e.getMessage());
        }

    }

    public void testRemoveMetadata() {
        Metadata met = new Metadata();
        met.addMetadata("Filename", "tempProduct");
        met.addMetadata("ProductStructure", "Flat");

        Product testProduct = getTestProduct();
        try {
            myCat.addProduct(testProduct);
            myCat.addMetadata(met, testProduct);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            myCat.removeMetadata(met, testProduct);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            Metadata retMet = myCat.getMetadata(testProduct);
            String retValue = retMet.getMetadata("Filename");
            assertNull(retValue);
        } catch (CatalogException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private static Product getTestProduct() {
        Product testProduct = Product.getDefaultFlatProduct("test",
                "urn:oodt:GenericFile");
        testProduct.getProductType().setName("GenericFile");

        // set references
        Reference ref = new Reference("file:///foo.txt", "file:///bar.txt", 100);
        Vector references = new Vector();
        references.add(ref);
        testProduct.setProductReferences(references);

        return testProduct;
    }

    private static Metadata getTestMetadata(String prodName) {
        Metadata met = new Metadata();
        met.addMetadata("CAS.ProductName", prodName);
        return met;
    }

}
