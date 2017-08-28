package com.agileapex.common.filedownloader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.Application;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.StreamResource;

// TODO: Replace this with Vaadin 7 FileDownloader functionality in the future

/**
 * HOW TO USE, EXAMPLE:
 * 
 *    final byte[] bytes = svgString.getBytes();
 *    String fileName = "Sprint Burndown Chart - " + dateUtil.formatToShortDate(new DateTime()) + ".svg";
 *    DynamicStreamResource streamResource = new DynamicStreamResource(bytes, fileName, DynamicStreamResource.MIME_TYPE_BINARY_DATA, getApplication(), 5000);
 *    getApplication().getMainWindow().open(streamResource, "_top");
 */
public class DynamicStreamResource extends StreamResource {
    private static final long serialVersionUID = -7902956717307457306L;
    public static final String MIME_TYPE_BINARY_DATA = "application/octet-stream";
    public static final String MIME_TYPE_PDF = "application/pdf";
    private final byte[] binaryData;
    private final String filename;

    public DynamicStreamResource(final byte[] binaryData, String filename, String mimeType, Application application, long cacheTime) {
        super(new StreamSource() {
            private static final long serialVersionUID = 4325848379444553L;

            @Override
            public InputStream getStream() {
                return new ByteArrayInputStream(binaryData);
            }
        }, filename, application);
        this.binaryData = binaryData;
        this.filename = filename;
        setMIMEType(mimeType);
        setCacheTime(cacheTime);
    }

    @Override
    public DownloadStream getStream() {
        final DownloadStream downloadStream = super.getStream();
        downloadStream.setParameter("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        downloadStream.setParameter("Content-Length", Integer.toString(binaryData.length));
        return downloadStream;
    }
}