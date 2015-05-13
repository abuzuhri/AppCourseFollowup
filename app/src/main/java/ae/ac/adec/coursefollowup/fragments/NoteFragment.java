package ae.ac.adec.coursefollowup.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.db.dal.ExamDao;
import ae.ac.adec.coursefollowup.db.dal.NoteDao;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.views.adapters.ExamAdapter;
import ae.ac.adec.coursefollowup.views.adapters.NoteAdapter;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by Tareq on 02/28/2015.
 */
public class NoteFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionsMenu fab;
    private int position;
    int noteType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(POSITION, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        OneFragmentActivity.temp = null;
        OneFragmentActivity.setCourseName(null);
        OneFragmentActivity.setNoteType(null);
        OneFragmentActivity.setFilePath(null);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FillDate();
            }
        }, 500);

    }

    private void FillDate() {
        NoteDao noteDao = new NoteDao();
        final List<Note> noteList = noteDao.getAll(position);
        mAdapter = new NoteAdapter(noteList, tf_roboto_light, getActivity(), new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                if(fab!=null)
                    fab.collapse();
                Note note = Note.load(Note.class, ID);
                if (note.NoteType == ConstantVariable.NoteType.Voice.id) {//Voice
                    AppAction.OpenActivityWithFRAGMENT(getActivity(), NoteFragmentViewVoice.class.getName(), ID);
                } else if (note.NoteType == ConstantVariable.NoteType.Text.id) {//Text
                    AppAction.OpenActivityWithFRAGMENT(getActivity(), NoteFragmentViewText.class.getName(), ID);
                } else if (note.NoteType == ConstantVariable.NoteType.Video.id) {//Video
                    AppAction.OpenActivityWithFRAGMENT(getActivity(), NoteFragmentViewVideo.class.getName(), ID);
                } else if (note.NoteType == ConstantVariable.NoteType.Image.id) {//Image
                    AppAction.OpenActivityWithFRAGMENT(getActivity(), NoteFragmentViewImage.class.getName(), ID);
                }
            }

        });
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        FillDate();
        //mRecyclerView.setItemAnimator(new FeedItemAnimator());

        fab = (FloatingActionsMenu) rootView.findViewById(R.id.fab);
        // floating buttons
        FloatingActionButton fab_text = new FloatingActionButton(getActivity());
        fab_text.setImageResource(R.drawable.text_icon);
        fab_text.setColorNormalResId(R.color.colorPrimary);
        fab_text.setColorPressedResId(R.color.material_drawer_primary_dark);
        FloatingActionButton fab_image = new FloatingActionButton(getActivity());
        fab_image.setImageResource(R.drawable.image_icon);
        fab_image.setColorNormalResId(R.color.colorPrimary);
        fab_image.setColorPressedResId(R.color.material_drawer_primary_dark);
        FloatingActionButton fab_video = new FloatingActionButton(getActivity());
        fab_video.setImageResource(R.drawable.video_icon);
        fab_video.setColorNormalResId(R.color.colorPrimary);
        fab_video.setColorPressedResId(R.color.material_drawer_primary_dark);
        FloatingActionButton fab_sound = new FloatingActionButton(getActivity());
        fab_sound.setImageResource(R.drawable.sound_icon);
        fab_sound.setColorNormalResId(R.color.colorPrimary);
        fab_sound.setColorPressedResId(R.color.material_drawer_primary_dark);

        fab.addButton(fab_text);
        fab.addButton(fab_image);
        fab.addButton(fab_video);
        fab.addButton(fab_sound);

        fab_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                noteType = ConstantVariable.NoteType.Text.id;
                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentAddEdit.class.getName(), -1, noteType);
            }
        });
        fab_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                noteType = ConstantVariable.NoteType.Image.id;
                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentAddEdit.class.getName(), -1, noteType);
            }
        });
        fab_sound.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                noteType = ConstantVariable.NoteType.Voice.id;
                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentAddEdit.class.getName(), -1, noteType);
            }
        });
        fab_video.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                noteType = ConstantVariable.NoteType.Video.id;
                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentAddEdit.class.getName(), -1, noteType);
            }
        });


        return rootView;
    }
}
