package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReportActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Bitmap bitmap;
    private Paint paint;
    private PdfDocument document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        paint = new Paint();
        document = new PdfDocument();

        // Meminta izin penyimpanan jika belum diberikan
        if (ContextCompat.checkSelfPermission(ReportActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReportActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    public void generatePDF(View view) {
        // Menghitung ukuran halaman PDF (ukuran A4)
        int pageWidth = 595;
        int pageHeight = 842;

        EditText etNamaKaryawan = findViewById(R.id.et_nama_karyawan);
        EditText etLokasi = findViewById(R.id.et_lokasi);
        EditText etWsid = findViewById(R.id.et_wsid);
        EditText etAlamat = findViewById(R.id.et_alamat);
        EditText ettypemesin = findViewById(R.id.et_type_mesin);
        EditText etserial_nomor = findViewById(R.id.et_serial_nomor);
        EditText etbarurelokasi = findViewById(R.id.et_baru_relokasi);
        EditText etteganganlistrik = findViewById(R.id.et_tegangan_listrik);
        EditText etgrounding = findViewById(R.id.et_grounding);
        EditText etinitialvsat = findViewById(R.id.et_initial_vsat);
        EditText etkonfigurasi = findViewById(R.id.et_konfigurasi);
        EditText etipadd = findViewById(R.id.et_ipaddress);
        EditText etcatridge = findViewById(R.id.et_catridge);
        EditText etreject = findViewById(R.id.et_reject);
        EditText etkunci = findViewById(R.id.et_kunci);
        EditText etkamera = findViewById(R.id.et_kamera);
        EditText etperekam = findViewById(R.id.et_perekam);
        EditText etpincover = findViewById(R.id.et_pin_cover);
        EditText etfdi = findViewById(R.id.et_fdi);
        EditText etmasterfdi = findViewById(R.id.et_master_key_fd);
        EditText etmasterinfofdi = findViewById(R.id.et_master_key_fd_info_ifno);
        EditText etac = findViewById(R.id.et_ac);
        EditText etneonsign = findViewById(R.id.et_neon_sign);
        EditText etstikermesin = findViewById(R.id.et_stiker_mesin);
        EditText etkabeljalur = findViewById(R.id.et_kabel_jalur);
        EditText etcolokanlistrik = findViewById(R.id.et_colokan_listrik);

        EditText etperekameksternal = findViewById(R.id.et_perekam_eksternal);
        EditText et_pe_merk = findViewById(R.id.et_merk_pe);
        EditText et_pe_sn = findViewById(R.id.et_sn_pe);

        EditText etkameraeksternal = findViewById(R.id.et_kamera_eksternal);
        EditText et_ke_merk = findViewById(R.id.et_merk_ke);
        EditText et_ke_sn = findViewById(R.id.et_sn_ke);

        EditText etups = findViewById(R.id.et_ups);
        EditText et_merk_ups = findViewById(R.id.et_merk_ups);
        EditText et_sn_ups = findViewById(R.id.et_sn_ups);

        EditText etmonitorled = findViewById(R.id.et_monitor_led);
        EditText et_merk_monitor = findViewById(R.id.et_merk_ml);
        EditText et_sn_monitor = findViewById(R.id.et_sn_ml);









        // Membuat halaman PDF baru
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Menggambar ke halaman PDF
        Canvas canvas = page.getCanvas();
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(16);

        String judul = "Berita Acara Instalasi ATM";
        float textWidth = textPaint.measureText(judul);
        float x = (pageWidth - textWidth) / 2;
        float y = 50;

        canvas.drawText(judul, x, y, textPaint);

        Paint subTextPaint = new Paint();
        subTextPaint.setColor(Color.BLACK);
        subTextPaint.setTextSize(12);

        String namakaryawan = ": " + etNamaKaryawan.getText().toString();
        String textNamakaryawan = "Nama Karyawan";
        float xNamaKaryawan = 30;
        float yNamaKaryawan = y + 40;

        String lokasi = ": "+etLokasi.getText().toString();
        String textLokasi = "Lokasi";
        float xLokasi = 30;
        float yLokasi = yNamaKaryawan + 20;

        String wsid = ": "+etWsid.getText().toString();
        String textwsid = "WSID";
        float xwsid = 30;
        float ywsid = yLokasi + 20;

        String alamat = ": "+etAlamat.getText().toString();
        String textalamat = "Alamat";
        float xalamat = 30;
        float yalamat = ywsid + 20;

        String typemesin = ": "+ettypemesin.getText().toString();
        String texttypemesin = "Type Mesin";
        float xtypemesin = 30;
        float ytypemesin = yalamat + 20;

        String serialnomor = ": "+etserial_nomor.getText().toString();
        String textserialnomor = "Serial Nomor";
        float xserialnomor = 30;
        float yserialnomor = ytypemesin + 20;

        String barurelokasi = ": "+etbarurelokasi.getText().toString();
        String textbarurelokasi = "Baru / Relokasi";
        float xbarurelokasi = 30;
        float ybarurelokasi = yserialnomor + 20;

        String teganganlistrik = ": "+etteganganlistrik.getText().toString()+" (UPS)";
        String textteganganlistrik = "Tegangan Listrik (UPS-IT)";
        float xteganganlistrik = 30;
        float yteganganlistrik = ybarurelokasi + 20;

        String grounding = ": "+etgrounding.getText().toString()+" (UPS)";
        String textgrounding = "Grounding  (UPS-IT)";
        float xgrounding = 30;
        float ygrounding = yteganganlistrik + 20;

        String initialvsat = ": "+etinitialvsat.getText().toString();
        String textinitialvsat = "Initial VSAT";
        float xinitialvsat = 30;
        float yinitialvsat = ygrounding + 20;

        String konfigurasi = ": "+etkonfigurasi.getText().toString();
        String textkonfigurasi = "Konfigurasi";
        float xkonfigurasi = 30;
        float ykonfigurasi = yinitialvsat + 20;

        String ipadd = ": "+etipadd.getText().toString();
        String textipadd = "IP Address";
        float xipadd = 30;
        float yipadd = ykonfigurasi + 20;

        String catridge = ": "+etcatridge.getText().toString();
        String textcatridge = "Catridge";
        float xcatridge = 30;
        float ycatridge = yipadd + 20;

        String reject = ": "+etreject.getText().toString();
        String textreject = "Reject";
        float xreject = 30;
        float yreject = ycatridge + 20;

        String kunci = ": "+etipadd.getText().toString();
        String textkunci = "Kunci";
        float xkunci = 30;
        float ykunci = yreject + 20;

        String kamera = ": "+etkamera.getText().toString();
        String textkamera = "Kamera";
        float xkamera = 30;
        float ykamera = ykunci + 20;

        String perekam = ": "+etperekam.getText().toString();
        String textperekam = "Perekam";
        float xperekam = 30;
        float yperekam = ykamera + 20;

        String pincover = ": "+etpincover.getText().toString();
        String textpincover = "Pin Cover";
        float xpincover = 30;
        float ypincover = yperekam + 20;

        String fdi = ": "+etfdi.getText().toString();
        String textfdi = "FDI";
        float xfdi = 30;
        float yfdi = ypincover + 20;


        String masterkey = ": "+etmasterfdi.getText().toString();
        String textmasterkey = "Master Key Triple desk";
        float xmasterkey = 30;
        float ymasterkey = yfdi + 20;

        String masterkeyinfo = ": "+etmasterinfofdi.getText().toString();
        String textmasterkeyinfo = "Harap info alasan jika tidak";
        float xmasterkeyinfo = 30;
        float ymasterkeyinfo = ymasterkey + 20;

        String ac = ": "+etac.getText().toString();
        String textac = "AC";
        float xac = 30;
        float yac = ymasterkeyinfo + 20;

        String textOutdoor = "Outdoor";
        float xoutdoor = 30;
        float youtdoor = yac + 20;

        String textneonsign = ": - Neon Sign ";
        String neonsign = ": "+etneonsign.getText().toString();

        String textindoor = "Indoor";
        float xindoor = 30;
        float yindoor = youtdoor + 20;

        String textstikermesin = ": - Stiker Mesin ";
        String stikermesin = ": "+etstikermesin.getText().toString();

        float xstikermesin = xindoor;
        float ystikermesin = yindoor;

        String textkabeljalur = "  - Kabel Jalur ";
        String kabeljalur = ": "+etkabeljalur.getText().toString();
        float xkabeljalur = xindoor;
        float ykabeljalur = yindoor + 20;


        String textcolokanlistrik = "  - Colokan Listrik ";
        String colokanlistrik = ": "+etcolokanlistrik.getText().toString();
        float xcolokanlistrik = xindoor;
        float ycolokanlistrik = ykabeljalur + 20;


        String textpe = "  - Perekam Eksternal ";
        String pe = ": "+etperekameksternal.getText().toString();
        float xpe = xindoor;
        float ype = ycolokanlistrik + 20;

        String textpe_merk = "  - Merk ";
        String pe_merk = ": "+et_pe_merk.getText().toString();
        float xpe_merk = xpe + 180;
        float ype_merk = ycolokanlistrik + 20;


        // set data
        canvas.drawText(textNamakaryawan, xNamaKaryawan, yNamaKaryawan, subTextPaint);
        canvas.drawText(namakaryawan, xNamaKaryawan + 170, yNamaKaryawan, subTextPaint);

        canvas.drawText(textLokasi, xLokasi, yLokasi, subTextPaint);
        canvas.drawText(lokasi, xLokasi + 170, yLokasi, subTextPaint);

        canvas.drawText(textwsid, xwsid, ywsid, subTextPaint);
        canvas.drawText(wsid, xwsid + 170, ywsid, subTextPaint);

        canvas.drawText(textalamat, xalamat, yalamat, subTextPaint);
        canvas.drawText(alamat, xalamat + 170, yalamat, subTextPaint);

        canvas.drawText(texttypemesin, xtypemesin, ytypemesin, subTextPaint);
        canvas.drawText(typemesin, xtypemesin + 170, ytypemesin, subTextPaint);

        canvas.drawText(textserialnomor, xserialnomor, yserialnomor, subTextPaint);
        canvas.drawText(serialnomor, xserialnomor + 170, yserialnomor, subTextPaint);

        canvas.drawText(textbarurelokasi, xbarurelokasi, ybarurelokasi, subTextPaint);
        canvas.drawText(barurelokasi, xbarurelokasi + 170, ybarurelokasi, subTextPaint);

        canvas.drawText(textteganganlistrik, xteganganlistrik, yteganganlistrik, subTextPaint);
        canvas.drawText(teganganlistrik, xteganganlistrik + 170, yteganganlistrik, subTextPaint);

        canvas.drawText(textgrounding, xgrounding, ygrounding, subTextPaint);
        canvas.drawText(grounding, xgrounding + 170, ygrounding, subTextPaint);

        canvas.drawText(textinitialvsat, xinitialvsat, yinitialvsat, subTextPaint);
        canvas.drawText(initialvsat, xinitialvsat + 170, yinitialvsat, subTextPaint);

        canvas.drawText(textkonfigurasi, xkonfigurasi, ykonfigurasi, subTextPaint);
        canvas.drawText(konfigurasi, xkonfigurasi + 170, ykonfigurasi, subTextPaint);

        canvas.drawText(textipadd, xipadd, yipadd, subTextPaint);
        canvas.drawText(ipadd, xipadd + 170, yipadd, subTextPaint);

        canvas.drawText(textcatridge, xcatridge, ycatridge, subTextPaint);
        canvas.drawText(catridge, xcatridge + 170, ycatridge, subTextPaint);

        canvas.drawText(textreject, xreject, yreject, subTextPaint);
        canvas.drawText(reject, xreject + 170, yreject, subTextPaint);

        canvas.drawText(textkunci, xkunci, ykunci, subTextPaint);
        canvas.drawText(kunci, xkunci + 170, ykunci, subTextPaint);

        canvas.drawText(textkamera, xkamera, ykamera, subTextPaint);
        canvas.drawText(kamera, xkamera + 170, ykamera, subTextPaint);

        canvas.drawText(textperekam, xperekam, yperekam, subTextPaint);
        canvas.drawText(perekam, xperekam + 170, yperekam, subTextPaint);

        canvas.drawText(textpincover, xpincover, ypincover, subTextPaint);
        canvas.drawText(pincover, xpincover + 170, ypincover, subTextPaint);

        canvas.drawText(textfdi, xfdi, yfdi, subTextPaint);
        canvas.drawText(fdi, xfdi + 170, yfdi, subTextPaint);

        canvas.drawText(textmasterkey, xmasterkey, ymasterkey, subTextPaint);
        canvas.drawText(masterkey, xmasterkey + 170, ymasterkey, subTextPaint);

        canvas.drawText(textmasterkeyinfo, xmasterkeyinfo, ymasterkeyinfo, subTextPaint);
        canvas.drawText(masterkeyinfo, xmasterkeyinfo + 170, ymasterkeyinfo, subTextPaint);

        canvas.drawText(textac, xac, yac, subTextPaint);
        canvas.drawText(ac, xac + 170, yac, subTextPaint);

        canvas.drawText(textOutdoor, xoutdoor, youtdoor, subTextPaint);
        canvas.drawText(textneonsign, xoutdoor + 170, youtdoor, subTextPaint);
        canvas.drawText(neonsign, xoutdoor + 300, youtdoor, subTextPaint);

        canvas.drawText(textindoor, xindoor, yindoor, subTextPaint);
        canvas.drawText(textstikermesin, xstikermesin + 170, ystikermesin, subTextPaint);
        canvas.drawText(stikermesin, xstikermesin + 300, ystikermesin, subTextPaint);


        canvas.drawText(textkabeljalur, xkabeljalur + 170, ykabeljalur, subTextPaint);
        canvas.drawText(kabeljalur, xkabeljalur + 300, ykabeljalur, subTextPaint);

        canvas.drawText(textcolokanlistrik, xcolokanlistrik + 170, ycolokanlistrik, subTextPaint);
        canvas.drawText(colokanlistrik, xcolokanlistrik + 300, ycolokanlistrik, subTextPaint);

        canvas.drawText(textpe, xpe + 170, ype, subTextPaint);
        canvas.drawText(pe, xpe + 300, ype, subTextPaint);

        canvas.drawText(textpe_merk, xpe_merk + 180, ype_merk, subTextPaint);
        canvas.drawText(pe_merk, xpe_merk + 220, ype_merk, subTextPaint);


        // Mengakhiri halaman PDF
        document.finishPage(page);

        // Menyimpan file PDF
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(directoryPath, "example.pdf");

        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF generated successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        }

        // Menutup dokumen PDF
        document.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, bisa melanjutkan
            } else {
                // Izin ditolak, berikan peringatan atau tanggapan sesuai kebutuhan aplikasi Anda
            }
        }
    }
}