package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;



import java.io.File;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.FullScreenImageActivity;
import ae.ac.adec.coursefollowup.activities.FullScreenVideoActivity;
import ae.ac.adec.coursefollowup.db.dal.NoteDao;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.services.dailogs.AppDialog;
import ae.ac.adec.coursefollowup.views.event.IDialogClick;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by MyLabtop on 4/1/2015.
 */
public class NoteFragmentViewImage extends BaseFragment {


    TextView txtNoteAddDate         =null;
    TextView txtNoteSubject         =null;
    TextView txtNoteDetail          =null;
    ImageView txtNoteImageView      =null;
    File imgFile =null;
    private int dC=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideSoftKeyboard();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_delete, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ic_menu_delete:
               AppDialog.Delete(getActivity(), new IDialogClick() {
                    @Override
                    public void onConfirm() {
                        Delete();
                    }
                });
                return true;
            case R.id.ic_menu_edit:
            //    Edit();
                return true;
            default:
                break;
        }

        return false;
    }

    public void Edit(){
        try {
     //       AppAction.OpenActivityWithFRAGMENT(getActivity(), TaskFragmentAddEdit.class.getName(), ID);
            getActivity().finish();
        }catch (Exception ex){
            Crouton.makeText(getActivity(), ex.getMessage(), Style.ALERT).show();
        }
    }

    public void Delete(){
        try {
            NoteDao noteDao = new NoteDao();

            noteDao.delete(ID);
            if(imgFile!=null)
            if(imgFile.exists()){
                imgFile.delete();
            }

            getActivity().finish();
            Toast.makeText(getActivity(), R.string.delete_successfully, Toast.LENGTH_LONG).show();
        }catch (BusinessRoleError ex){
            AppAction.DiaplayError(getActivity(), ex.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private  void fillDate(){
        if(ID!=null && ID!=0){

            Note note= Note.load(Note.class, ID);

            txtNoteAddDate.setText(ConstantVariable.getDateString(note.DateAdded));
            txtNoteSubject.setText(note.Course.Name);
            txtNoteDetail.setText(note.Details);

            imgFile = new  File(note.FilePath);
            if(imgFile.exists()){

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

               Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
                Drawable d = new BitmapDrawable(getResources(),myBitmap);
                txtNoteImageView.setImageDrawable(d);
                txtNoteImageView.setRotation(90);


            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_image, container, false);
        removeShadowForNewApi21(rootView);

        txtNoteAddDate= (TextView) rootView.findViewById(R.id.txtNoteAddDate);

        txtNoteSubject= (TextView) rootView.findViewById(R.id.txtNoteSubject);

        txtNoteDetail = (TextView) rootView.findViewById(R.id.txtNoteDetail);

        txtNoteImageView = (ImageView) rootView.findViewById(R.id.txtNoteImageView);

        txtNoteImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                dC++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        dC = 0;
                    }
                };

                if (dC == 1) {
                    //Single click
                    handler.postDelayed(r, 250);
                } else if (dC == 2) {
                    //Double click
                    dC = 0;

                    Intent i = new Intent(getActivity(), FullScreenImageActivity.class);
                    i.putExtra("imagePath", imgFile.getPath());


                    getActivity().startActivity(i);

                }

                return false;
            }
        });


        fillDate();

        return rootView;
    }

}
