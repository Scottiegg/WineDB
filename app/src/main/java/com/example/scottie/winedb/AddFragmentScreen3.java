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

/**
 * AddFragmentScreen3
 * <p>
 * Used by parent activity (AddActivity)
 * to allow user to enter in a new wine into app.
 * <p>
 * Has optional fields only
 * <p>
 * Provides interface for callback to parent activity
 *
 * @author Scott Garton
 * @version 1.0
 */
public class AddFragmentScreen3 extends Fragment {
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

    public AddFragmentScreen3() {
    }

    OnNextClickListenerScreen3 activityCallback;

    /**
     * OnNextClickListenerScreen3
     * <p>
     * Interface used to call-back to parent activity
     */
    public interface OnNextClickListenerScreen3 {
        void onNextButtonClickScreen3(Bundle savedData);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityContext = activity.getBaseContext();

        // attach listener
        try {
            activityCallback = (OnNextClickListenerScreen3) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement SearchClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_screen3, container, false);
        getViewsById(view);
        initialiseUI();
        return view;
    }

    /**
     * getViewsById
     * <p>
     * Method to assign Views for each UI component in Fragment
     *
     * @param view View after fragment inflate
     */
    private void getViewsById(View view) {
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

    /**
     * initialiseUI
     * <p>
     * Method used to set callback for buttons, as part of initializing the UI
     */
    private void initialiseUI() {
        nextButton01.setOnClickListener(new OnNextClickListener());
        nextButton02.setOnClickListener(new OnNextClickListener());

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddImage();
            }
        });
    }

    /**
     * OnNextClickListener
     * <p>
     * Inner class used to facilitate logic for when callback to parent activity is activated
     *
     * @author Scott Garton
     * @version 1.0
     */
    class OnNextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Load up bundle with data
            Bundle bundle = new Bundle();
            if (!(Utils.isEmpty(notes)))
                bundle.putString("NOTES", notes.getText().toString());

            bundle.putFloat("RATING", ratingBar.getRating());

            if (!(Utils.isEmpty(pricePaid)))
                bundle.putString("PRICE_PAID", pricePaid.getText().toString());

            // Put image of bottle
            if (bottleImage != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bottleImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] bytes = stream.toByteArray();
                bundle.putSerializable("IMAGE", bytes);
            }

            if (!(Utils.isEmpty(drinkFrom)))
                bundle.putString("DRINK_FROM", drinkFrom.getText().toString());

            if (!(Utils.isEmpty(drinkTo)))
                bundle.putString("DRINK_TO", drinkTo.getText().toString());

            if (!(Utils.isEmpty(yearBought)))
                bundle.putString("YEAR_BOUGHT", yearBought.getText().toString());

            if (!(Utils.isEmpty(percentAlcohol)))
                bundle.putString("PERCENT_ALCOHOL", percentAlcohol.getText().toString());

            // activate callback
            activityCallback.onNextButtonClickScreen3(bundle);
        }
    }

    /**
     * onAddImage
     * <p>
     * Method used to get image from device camera or device gallery via intents
     * (by starting a new activity).
     * <p>
     * Shows dialog to user.
     */
    private void onAddImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            outputFileUri = Uri.fromFile(f);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                            startActivityForResult(intent, 1);
                        } else if (options[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                }).show();
    }

    /**
     * onActivityResult
     * <p>
     * Processes results from any outstanding intent requests (I.e. Activity results)
     *
     * @param requestCode provided in onAddImage when startActivityForResult() was called.
     * @param resultCode  either RESULT_OK or RESULT_CANCELLED
     * @param data        the stuff we care about (as an Intent, provided in extras)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                File imageFile = new File(outputFileUri.getPath());
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                Bitmap tempImage = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bitmapOptions);
                bottleImage = resizeImage(tempImage);
            } else if (requestCode == 2) {
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

    /**
     * resizeImage
     * <p>
     * Method used to rescale image so that the device doesn't fill up with too much data!
     *
     * @param inBitmap original bitmap
     * @return a resized bitmap
     */
    private Bitmap resizeImage(Bitmap inBitmap) {
        final int maxSize = 960;
        int outWidth;
        int outHeight;

        int inWidth = inBitmap.getWidth();
        int inHeight = inBitmap.getHeight();

        // make sure longest edge does not exceed 960px
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        return Bitmap.createScaledBitmap(inBitmap, outWidth, outHeight, false);
    }


}
