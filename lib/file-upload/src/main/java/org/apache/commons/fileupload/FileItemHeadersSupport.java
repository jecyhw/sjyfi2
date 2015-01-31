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

/**
 * Interface that will indicate that {@link org.apache.commons.fileupload.FileItem} or {@link org.apache.commons.fileupload.FileItemStream}
 * implementations will accept the headers read for the item.
 *
 * @since 1.2.1
 *
 * @see org.apache.commons.fileupload.FileItem
 * @see org.apache.commons.fileupload.FileItemStream
 *
 * @version $Id$
 */
public interface FileItemHeadersSupport {

    /**
     * Returns the collection of headers defined locally within this item.
     *
     * @return the {@link org.apache.commons.fileupload.FileItemHeaders} present for this item.
     */
    FileItemHeaders getHeaders();

    /**
     * Sets the headers read from within an item.  Implementations of
     * {@link org.apache.commons.fileupload.FileItem} or {@link org.apache.commons.fileupload.FileItemStream} should implement this
     * interface to be able to get the raw headers found within the item
     * header block.
     *
     * @param headers the instance that holds onto the headers
     *         for this instance.
     */
    void setHeaders(FileItemHeaders headers);

}
