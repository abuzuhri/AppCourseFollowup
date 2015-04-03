package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.NoteDao;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.services.dailogs.AppDialog;
import ae.ac.adec.coursefollowup.views.event.IDialogClick;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import ae.ac.adec.coursefollowup.views.view.CustomEditText;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by MyLabtop on 4/3/2015.
 */
public class NoteFragmentViewText extends BaseFragment {


    TextView txtNoteAddDate         =null;
    TextView txtNoteSubject         =null;
    TextView txtNoteDetail          =null;
    CustomEditText customEditText   =null;

    String filepath = null;
    File txtFile =null;

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


    private String readFromFile() {

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(txtFile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return text.toString();
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
            if(txtFile!=null)
                if(txtFile.exists()){
                    txtFile.delete();
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
//            txtNoteDetail.setText(note.Details);

            txtFile = new  File(note.FilePath);
            customEditText.setText(readFromFile());


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_text_view, container, false);
        removeShadowForNewApi21(rootView);


        txtNoteAddDate= (TextView) rootView.findViewById(R.id.txtNoteAddDate);

        txtNoteSubject= (TextView) rootView.findViewById(R.id.txtNoteSubject);



        LinearLayout v = (LinearLayout)rootView.findViewById(R.id.compoundView);

        customEditText = new CustomEditText(getActivity(),null);
        customEditText.setLines(30);
        customEditText.setGravity(Gravity.LEFT | Gravity.TOP);
        v.addView(customEditText);

        fillDate();

      //  txtNoteImageView = (ImageView) rootView.findViewById(R.id.txtNoteImageView);






        return rootView;
    }

}
