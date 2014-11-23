package Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

public class DialogFactory{


    public static DialogFragment createDialogCustomYesNo(View view, Context context, CoreCallback cb){
        // first callback is for Yes
        return CustomYesNoDialogFragment.newInstance(view, context, cb, null);
    }

    public static DialogFragment createDialogCustomYesNo(View view, Context context, CoreCallback cb, CoreCallback cbNo){
        // first callback is for Yes and second is for No
        return CustomYesNoDialogFragment.newInstance(view, context, cb, cbNo);
    }

    public static DialogFragment createDialogYesNo(String message, CoreCallback cb){
           // first callback is for Yes
           return YesNoDialogFragment.newInstance(message, cb, null);
    }

    public static DialogFragment createDialogYesNo(String message, CoreCallback cb, CoreCallback cbNo){
        // first callback is for Yes and second is for No
        return YesNoDialogFragment.newInstance(message, cb, cbNo);
    }

    public static DialogFragment createDialogOk(String message, CoreCallback cb){
        // first callback is for Ok
        return OkDialogFragment.newInstance(message, cb);
    }

    public static DialogFragment createDialogOk(String message){

        return OkDialogFragment.newInstance(message, null);
    }

    public static class OkDialogFragment extends DialogFragment {
        private String message;
        private CoreCallback cb;

        public OkDialogFragment setDialog(String message, CoreCallback cb){
            this.message = message;
            this.cb = cb;

            return this;
        }

        public static OkDialogFragment newInstance(String message, CoreCallback cb) {

            return new OkDialogFragment().setDialog(message, cb);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage(message)

                            // User cannot dismiss dialog by hitting back button
                    .setCancelable(false)

                            // Set up Yes Button
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    if (cb != null) {
                                        cb.run();
                                    }
                                }
                            }).create();
        }
    }

    public static class CustomYesNoDialogFragment extends DialogFragment {
        private View view;
        private CoreCallback cb;
        private CoreCallback cbNo;
        private Context context;

        public CustomYesNoDialogFragment setDialog(View view, Context context, CoreCallback cb, CoreCallback cbNo){
            this.view = view;
            this.context = context;
            this.cb = cb;
            this.cbNo = cbNo;

            return this;
        }

        public static CustomYesNoDialogFragment newInstance(View view, Context context, CoreCallback cb, CoreCallback cbNo) {

            return new CustomYesNoDialogFragment().setDialog(view, context, cb, cbNo);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(context)
                            // User cannot dismiss dialog by hitting back button
                    .setCancelable(false)

                            // Set up No Button
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    if (cbNo != null) {
                                        cbNo.run();
                                    }
                                }
                            })
                    .setView(view)
                            // Set up Yes Button
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    if (cb != null) {
                                        cb.run();
                                    }
                                }
                            }).create();
        }
    }

    public static class YesNoDialogFragment extends DialogFragment {
        private String message;
        private CoreCallback cb;
        private CoreCallback cbNo;

        public YesNoDialogFragment setDialog(String message, CoreCallback cb, CoreCallback cbNo){
            this.message = message;
            this.cb = cb;
            this.cbNo = cbNo;

            return this;
        }

        public static YesNoDialogFragment newInstance(String message, CoreCallback cb, CoreCallback cbNo) {

            return new YesNoDialogFragment().setDialog(message, cb, cbNo);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage(message)

                            // User cannot dismiss dialog by hitting back button
                    .setCancelable(false)

                            // Set up No Button
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    if (cbNo != null) {
                                        cbNo.run();
                                    }
                                }
                            })

                            // Set up Yes Button
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    if (cb != null) {
                                        cb.run();
                                    }
                                }
                            }).create();
        }
    }

}

