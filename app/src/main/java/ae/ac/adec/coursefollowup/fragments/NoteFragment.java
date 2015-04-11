package ae.ac.adec.coursefollowup.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int position;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(POSITION, 0);
    }

    @Override
    public void onResume() {
        super.onResume();

        OneFragmentActivity.temp=null;
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
        final List<Note> noteList= noteDao.getAll(position);


        mAdapter = new NoteAdapter(noteList,getActivity(),new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {

                Note note= Note.load(Note.class, ID);

                if(note.NoteType==1){//Voice

                       AppAction.OpenActivityWithFRAGMENT(getActivity(), NoteFragmentViewVoice.class.getName(), ID);

                }else if(note.NoteType==2){//Text

                    AppAction.OpenActivityWithFRAGMENT(getActivity(), NoteFragmentViewText.class.getName(), ID);


                }else if(note.NoteType==3){//Video

                    AppAction.OpenActivityWithFRAGMENT(getActivity(), NoteFragmentViewVideo.class.getName(), ID);

                }else if(note.NoteType==4){//Image

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

        FloatingActionButton fab=(FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentAddEdit.class.getName(),-1);

            }
        });

        return rootView;
    }
}
