package com.example.scottie.winedb;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class AddFragmentScreen3 extends Fragment
{
    private Context activityContext;

    private Button nextButton01;
    private Button nextButton02;
    private EditText notes;
    private RatingBar ratingBar;
    private EditText pricePaid;
    private Button addImageButton;
    private EditText drinkFrom;
    private EditText drinkTo;
    private EditText yearBought;
    private EditText percentAlcohol;

    private Bitmap bottleImage = null;

    private Uri outputFileUri;
    private int CAMERA_REQUEST = 4000;

    //TODO: store bitmap in field until finish clicked

    public AddFragmentScreen3() {}

    OnNextClickListenerScreen3 activityCallback;

    public interface OnNextClickListenerScreen3
    {
        void onNextButtonClickScreen3(Bundle savedData);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        activityContext = activity.getBaseContext();

        try
        {
            activityCallback = (OnNextClickListenerScreen3) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + "must implement SearchClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_screen3, container, false);
        getViewsById(view);
        initialiseUI();
        return view;
    }

    private void getViewsById(View view)
    {
        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.overall_layout);
        LinearLayout firstLayout = (LinearLayout) tableLayout.findViewById(R.id.LinearLayout01);
        TableRow tableRow01 = (TableRow) tableLayout.findViewById(R.id.TableRow01);
        TableRow tableRow03 = (TableRow) tableLayout.findViewById(R.id.TableRow03);
        TableRow tableRow05 = (TableRow) tableLayout.findViewById(R.id.TableRow05);
        LinearLayout lastLayout = (LinearLayout) tableLayout.findViewById(R.id.LinearLayout02);

        nextButton01 = (Button) firstLayout.findViewById(R.id.nextButton01);
        nextButton02 = (Button) lastLayout.findViewById(R.id.nextButton02);
        notes = (EditText) firstLayout.findViewById(R.id.notesField);
        ratingBar = (RatingBar) firstLayout.findViewById(R.id.ratingBar);
        pricePaid = (EditText) tableRow01.findViewById(R.id.pricePaidField);
        addImageButton = (Button) tableRow01.findViewById(R.id.addImageButton);
        drinkFrom = (EditText) tableRow03.findViewById(R.id.drinkFromField);
        drinkTo = (EditText) tableRow03.findViewById(R.id.drinkToField);
        yearBought = (EditText) tableRow05.findViewById(R.id.yearBoughtField);
        percentAlcohol = (EditText) tableRow05.findViewById(R.id.percentAlcoholField);
    }

    private void initialiseUI()
    {
        nextButton01.setOnClickListener(new OnNextClickListener());
        nextButton02.setOnClickListener(new OnNextClickListener());

        addImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onAddImage();
            }
        });
    }

    class OnNextClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Bundle bundle = new Bundle();
            if (!(isEmpty(notes)))
                bundle.putString("NOTES", notes.getText().toString());

            bundle.putFloat("RATING", ratingBar.getRating());

            if (!(isEmpty(pricePaid)))
                bundle.putString("PRICE_PAID", pricePaid.getText().toString());

            if (bottleImage != null)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bottleImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] bytes = stream.toByteArray();
                bundle.putSerializable("IMAGE", bytes);
            }

            if (!(isEmpty(drinkFrom)))
                bundle.putString("DRINK_FROM", drinkFrom.getText().toString());

            if (!(isEmpty(drinkTo)))
                bundle.putString("DRINK_TO", drinkTo.getText().toString());

            if (!(isEmpty(yearBought)))
                bundle.putString("YEAR_BOUGHT", yearBought.getText().toString());

            if (!(isEmpty(percentAlcohol)))
                bundle.putString("PERCENT_ALCOHOL", percentAlcohol.getText().toString());

            activityCallback.onNextButtonClickScreen3(bundle);
        }
    }

    private void onAddImage()
    {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item)
                    {
                        if (options[item].equals("Take Photo"))
                        {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            outputFileUri = Uri.fromFile(f);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                            startActivityForResult(intent, 1);
                        }
                        else if (options[item].equals("Choose from Gallery"))
                        {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                        }
                        else if (options[item].equals("Cancel"))
                        {
                            dialog.dismiss();
                        }
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 1)
            {
                File imageFile = new File(outputFileUri.getPath());
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                Bitmap tempImage = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bitmapOptions);
                bottleImage = resizeImage(tempImage);
            }
            else if (requestCode == 2)
            {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                Bitmap tempImage = BitmapFactory.decodeFile(picturePath);
                bottleImage = resizeImage(tempImage);
            }
        }
    }

    private Bitmap resizeImage(Bitmap inBitmap)
    {
        final int maxSize = 960;
        int outWidth;
        int outHeight;

        int inWidth = inBitmap.getWidth();
        int inHeight = inBitmap.getHeight();

        if(inWidth > inHeight)
        {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        }
        else
        {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        return Bitmap.createScaledBitmap(inBitmap, outWidth, outHeight, false);
    }

    private boolean isEmpty(EditText text)
    {
        String s = text.getText().toString().trim();
        return s.matches("");
    }
}
