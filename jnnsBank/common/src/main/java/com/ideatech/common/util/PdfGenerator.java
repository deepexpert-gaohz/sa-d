package com.ideatech.common.util;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author liangding
 * @create 2018-07-12 下午5:27
 **/
@Slf4j
public abstract class PdfGenerator {
    public static byte[] generate(byte[] template, Map<String, Object> context) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument document = new PdfDocument(new PdfReader(new ByteArrayInputStream(template)), new PdfWriter(baos));
        PdfAcroForm form = PdfAcroForm.getAcroForm(document, false);
        PdfFont f2 = PdfFontFactory.createFont("fonts/simsun.ttc,1", PdfEncodings.IDENTITY_H, false);
        for (String key : context.keySet()) {
            PdfFormField field = form.getField(key);
            if (field != null) {
                field.setFont(f2);
                field.setValue("" + context.get(key));
            }
        }
        form.flattenFields();
        document.close();
        return baos.toByteArray();
    }
}