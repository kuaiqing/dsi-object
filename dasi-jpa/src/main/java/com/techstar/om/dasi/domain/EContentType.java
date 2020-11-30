package com.techstar.om.dasi.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;

public enum EContentType {
    JSON(ContentType.APPLICATION_JSON.getMimeType()),
    XML(ContentType.APPLICATION_XML.getMimeType()),
    TEXT_HTML(ContentType.TEXT_HTML.getMimeType()),
    TEXT_XML(ContentType.TEXT_XML.getMimeType()),
    TEXT_PLAIN(ContentType.TEXT_PLAIN.getMimeType()),

    FORM_URLENCODED(ContentType.APPLICATION_FORM_URLENCODED.getMimeType()),
    MULTIPART_FORM_DATA(ContentType.MULTIPART_FORM_DATA.getMimeType()),

    ATOM_XML(ContentType.APPLICATION_ATOM_XML.getMimeType()),
    OCTET_STREAM(ContentType.APPLICATION_OCTET_STREAM.getMimeType()),
    SOAP_XML(ContentType.APPLICATION_SOAP_XML.getMimeType()),
    SVG_XML(ContentType.APPLICATION_SVG_XML.getMimeType()),
    XHTML_XML(ContentType.APPLICATION_XHTML_XML.getMimeType()),

    IMAGE_BMP(ContentType.IMAGE_BMP.getMimeType()),
    IMAGE_GIF(ContentType.IMAGE_GIF.getMimeType()),
    IMAGE_JPEG(ContentType.IMAGE_JPEG.getMimeType()),
    IMAGE_PNG(ContentType.IMAGE_PNG.getMimeType()),
    IMAGE_SVG(ContentType.IMAGE_SVG.getMimeType()),
    IMAGE_TIFF(ContentType.IMAGE_TIFF.getMimeType()),
    IMAGE_WEBP(ContentType.IMAGE_WEBP.getMimeType()),

    WILDCARD(ContentType.WILDCARD.getMimeType()),
    ;


    private String mimeType;

    EContentType(String mimeType) {
        this.mimeType = mimeType;
    }

    public static EContentType typeOf(String mimeType) {
        for (EContentType type : values()) {
            if (StringUtils.startsWithIgnoreCase(mimeType, type.mimeType)) {
                return type;
            }
        }
        return TEXT_PLAIN;
    }

}