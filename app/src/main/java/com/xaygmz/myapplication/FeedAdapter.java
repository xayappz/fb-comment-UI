package com.xaygmz.myapplication;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private final ArrayList<FeedModal> facebookFeedModalArrayList;
    private final Context context;
    public AppBarLayout appBarLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private NestedScrollView nestedScrollView;
    private CoordinatorLayout coordinatorLayout;

    public FeedAdapter(ArrayList<FeedModal> facebookFeedModalArrayList, Context context) {
        this.facebookFeedModalArrayList = facebookFeedModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new FeedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        FeedModal modal = facebookFeedModalArrayList.get(position);

        Picasso.get().load(modal.getAuthorImage()).into(holder.authorIV);
        holder.authorNameTV.setText(modal.getAuthorName());
        holder.timeTV.setText(modal.getPostDate());
        holder.descTV.setText(modal.getPostDescription());
        holder.postIV.setImageDrawable(context.getDrawable(R.drawable.ic_android_black_24dp));
        holder.likesTV.setText(modal.getPostLikes());
        holder.readcmnts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                LayoutInflater factory = LayoutInflater.from(context);
//                View deleteDialogView = factory.inflate(R.layout.mycmnts, null);
//                AlertDialog dialog = builder.create();
//                dialog.setView(deleteDialogView);
//                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//                dialog.show();
                //showComments();
                // showBottomSheetDialog();
                show();
            }
        });
        holder.commentsTV.setText(modal.getPostComments());
    }

    private void show() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = li.inflate(R.layout.mycmnts, null);
        bottomSheetDialog.setContentView(contentView);
        bottomSheetDialog.setCancelable(true);
        coordinatorLayout = contentView.findViewById(R.id.cordin);
        appBarLayout = contentView.findViewById(R.id.appbar);
        nestedScrollView = contentView.findViewById(R.id.scroll);
        nestedScrollView = contentView.findViewById(R.id.scroll);
        nestedScrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {

                        if (nestedScrollView.getChildAt(0).getBottom()
                                <= (nestedScrollView.getHeight() + nestedScrollView.getScrollY())) {
                            //Log.e("YES","BOTTOM");
                            appBarLayout.setOnTouchListener(new View.OnTouchListener() {
                                @SuppressLint("ClickableViewAccessibility")
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    Log.i("EVENT", event.getAction() + "..");

                                    switch (event.getActionMasked()) {

                                        case MotionEvent.ACTION_DOWN:
//

                                            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                                                @Override
                                                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                                                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {// ... do whatever is required on 'expanded'
                                                    } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                                        bottomSheetDialog.dismiss();
                                                    }
                                                }

                                                @SuppressLint("ClickableViewAccessibility")
                                                @Override
                                                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                                                    if (slideOffset == 1.0) {
                                                        bottomSheetBehavior.onNestedFling(coordinatorLayout, appBarLayout, null, 100, 10000, true);
                                                    }

                                                }
                                            });
                                            break;
                                        case MotionEvent.ACTION_UP:
                                            break;

                                        default:
                                            return false;
                                    }
                                    return true;
                                }
                            });

                        } else {
                            // Toast.makeText(context, "keep scrolling", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                bottomSheetBehavior = BottomSheetBehavior.from((View) contentView.getParent());
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                int height = displayMetrics.heightPixels;

                int maxHeight = (int) (height * 0.88);
                bottomSheetBehavior.setPeekHeight(maxHeight);
                contentView.requestLayout();


                // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

// Get your contentView children set here...
        bottomSheetDialog.show();
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return facebookFeedModalArrayList.size();
    }

    private void showComments() {
        LayoutInflater layoutInflater =
                (LayoutInflater) context
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.mycmnts, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY, 50, 0);
        popupWindow.setAnimationStyle(R.style.DialogAnimation);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            int orgX, orgY;
            int offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN:
                        orgX = (int) event.getX();
                        orgY = (int) event.getY();
                        popupWindow.update(orgX, orgY, -1, -100, true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetX = (int) event.getRawX() - orgX;
                        offsetY = (int) event.getRawY() - orgY;
                        popupWindow.update(offsetX, offsetY, -1, -1, true);
                }
                return true;
            }
        });
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.mycmnts);
        View view = View.inflate(context, R.layout.mycmnts, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(((View) view.getParent()));
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        int height = displayMetrics.heightPixels;

        int maxHeight = (int) (height * 0.88);

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());

        mBehavior.setPeekHeight(maxHeight);
        bottomSheetDialog.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views
        // of recycler view items.
        private final CircleImageView authorIV;
        private final TextView authorNameTV;
        private final TextView timeTV;
        private final TextView descTV;
        private final ImageView postIV;
        private final TextView likesTV;
        private final TextView commentsTV;
        private final LinearLayout readcmnts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our variables
            readcmnts = itemView.findViewById(R.id.readcmnts);
            authorIV = itemView.findViewById(R.id.idCVAuthor);
            authorNameTV = itemView.findViewById(R.id.idTVAuthorName);
            timeTV = itemView.findViewById(R.id.idTVTime);
            descTV = itemView.findViewById(R.id.idTVDescription);
            postIV = itemView.findViewById(R.id.idIVPost);
            likesTV = itemView.findViewById(R.id.idTVLikes);
            commentsTV = itemView.findViewById(R.id.idTVComments);
        }
    }

}