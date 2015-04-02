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

import java.util.List;

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

                if(noteList.get(((int)ID)-1).NoteType==1){//Voice

                    //   AppAction.OpenActivityWithFRAGMENT(getActivity(), TaskFragmentView.class.getName(), ID);

                }else if(noteList.get(((int)ID)-1).NoteType==2){//Text

                    //   AppAction.OpenActivityWithFRAGMENT(getActivity(), TaskFragmentView.class.getName(), ID);

                }else if(noteList.get(((int)ID)-1).NoteType==3){//Video

                    //   AppAction.OpenActivityWithFRAGMENT(getActivity(), TaskFragmentView.class.getName(), ID);

                }else if(noteList.get(((int)ID)-1).NoteType==4){//Image

                      AppAction.OpenActivityWithFRAGMENT(getActivity(), NoteFragmentViewImage.class.getName(), ID);

                }
         //      Toast.makeText(getActivity(),"ID = "+ID+"   Pos = "+noteList.get(((int)ID)-1).NoteType+"",Toast.LENGTH_LONG).show();
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
