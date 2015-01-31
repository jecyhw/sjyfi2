/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.fileupload;

import java.io.IOException;

/**
 * An iterator, as returned by
 * {@link org.apache.commons.fileupload.FileUploadBase#getItemIterator(org.apache.commons.fileupload.RequestContext)}.
 *
 * @version $Id: FileItemIterator.java 1454691 2013-03-09 12:15:54Z simonetripodi $
 */
public interface FileItemIterator {

    /**
     * Returns, whether another instance of {@link org.apache.commons.fileupload.FileItemStream}
     * is available.
     *
     * @throws org.apache.commons.fileupload.FileUploadException Parsing or processing the
     *   file item failed.
     * @throws java.io.IOException Reading the file item failed.
     * @return True, if one or more additional file items
     *   are available, otherwise false.
     */
    boolean hasNext() throws FileUploadException, IOException;

    /**
     * Returns the next available {@link org.apache.commons.fileupload.FileItemStream}.
     *
     * @throws java.util.NoSuchElementException No more items are available. Use
     * {@link #hasNext()} to prevent this exception.
     * @throws org.apache.commons.fileupload.FileUploadException Parsing or processing the
     *   file item failed.
     * @throws java.io.IOException Reading the file item failed.
     * @return FileItemStream instance, which provides
     *   access to the next file item.
     */
    FileItemStream next() throws FileUploadException, IOException;

}
