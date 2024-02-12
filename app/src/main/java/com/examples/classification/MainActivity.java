package org.tensorflow.lite.examples.classification;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.tensorflow.lite.examples.classification.tflite.Classifier;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;





public class MainActivity extends AppCompatActivity {
    private static final int RESULT_PICK_IMAGEFILE = 1000;
    private ImageView imageView;
    private Classifier classifier;
    private Classifier.Model model = Classifier.Model.QUANTIZED;
    private Classifier.Device device = Classifier.Device.CPU;
    private int numThreads = -1;
    TextView tv, tv1, tv2 ,tv3, tv4, tv5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view);
        tv = (TextView)findViewById(R.id.textView);
        tv1 = (TextView)findViewById(R.id.textView2);
        tv2 = (TextView)findViewById(R.id.textView3);
        tv3 = (TextView)findViewById(R.id.textView4);
        tv4 = (TextView)findViewById(R.id.textView5);
        tv5 = (TextView)findViewById(R.id.textView6);


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
            }
        });

        try {
            classifier = Classifier.create(this, model, device, numThreads);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    Bitmap bmp = getBitmapFromUri(uri);
                    Bitmap afterResizeBitmap = Bitmap.createScaledBitmap(bmp,
                            224,
                            224,
                            true);

                    final List<Classifier.Recognition> results = classifier.recognizeImage(afterResizeBitmap);
                    results.get(0).getConfidence();
                    results.get(1).getConfidence();
                    results.get(2).getConfidence();

                    imageView.setImageBitmap(afterResizeBitmap);

                    String text = results.get(0).getTitle();
                    String text1 = results.get(1).getTitle();
                    String text2 = results.get(2).getTitle();

                    tv.setText(text);
                    tv1.setText(text1);
                    tv2.setText(text2);


                    /**getNormalizedProbability(0);
                    getNormalizedProbability(1);
                    getNormalizedProbability(2);

                    String text3 =  String.valueOf(getNormalizedProbability(0));
                    String text4 =  String.valueOf(getNormalizedProbability(1));
                    String text5 =  String.valueOf(getNormalizedProbability(2));

                    tv3.setText(text3);
                    tv4.setText(text4);
                    tv5.setText(text5);**/


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }



    private Bitmap getBitmapFromUri(Uri uri) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }



    /**
     * Run inference using the prepared input in {@link #imgData}. Afterwards, the result will be
     * provided by getProbability().
     *
     * <p>This additional method is necessary, because we don't have a common base for different
     * primitive data types.
     */
}

