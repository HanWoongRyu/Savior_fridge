package com.example.myRefri02;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Editor_Adapter extends RecyclerView.Adapter<Editor_Adapter.ItemViewHolder>{

    private ArrayList<Data> listData = new ArrayList<>();
    public Context context;
    private int position = RecyclerView.NO_POSITION;
    private Data data;
    public String impo;
//    OnItemClickListener listener;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refri_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    String getItemopsition(int position){
        return listData.get(position).getTitle();
    }
    String getContentopsition(int position){
        return listData.get(position).getContent();
    }

    void addItem(Data data){
        listData.add(data);
    }

    Data getItem(int position){
        return listData.get(position);
    }

    public int getPosition(){
        return position;
    }

    public static void filesave(Context context, String filename, String text) {
        try{
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    // ?????? ?????? ??????

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        private TextView tvTitle;
        private TextView tvContent;

        public ItemViewHolder(View itemView){
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvtitle);
            tvContent = itemView.findViewById(R.id.tvcontent);

            itemView.setOnCreateContextMenuListener(this);
        }

        void onBind(Data data){
            tvTitle.setText(data.getTitle());
            tvContent.setText(data.getContent());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem Edit = contextMenu.add(Menu.NONE, 1001, 1, "??????");
            MenuItem Delete = contextMenu.add(Menu.NONE, 1002, 2, "??????");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case 1001: // ?????? ??????

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View view = LayoutInflater.from(context).inflate(R.layout.editbox, null, false);
                        builder.setView(view);

                        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
                        EditText editTitle = (EditText) view.findViewById(R.id.editTitle);
                        EditText editContent = (EditText) view.findViewById(R.id.editContent);

                        impo = listData.get(getAbsoluteAdapterPosition()).getTitle() + "," +listData.get(getAbsoluteAdapterPosition()).getContent()+"->";

                        Log.w("??????", impo);

                        editTitle.setText(listData.get(getAbsoluteAdapterPosition()).getTitle());
                        editContent.setText(listData.get(getAbsoluteAdapterPosition()).getContent());

                        AlertDialog dialog = builder.create();
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String strTitle = editTitle.getText().toString();
                                String strContent = editContent.getText().toString();

                                impo = impo + strTitle +","+ strContent;
                                Log.w("??????1", impo);
                                try{
                                    File f = new File("/data/data/com.example.myRefri02/files/editfile.txt");
                                    if(f.exists()){
                                        Log.w("??????", "??????");
                                        FileOutputStream fos = context.openFileOutput(f.getName(), Context.MODE_APPEND);
                                        fos.write(',');
                                        fos.write(impo.getBytes());
                                        fos.close();
                                    }
                                    else{
                                        Log.w("??????", "??????");
                                        FileOutputStream fos = context.openFileOutput(f.getName(), Context.MODE_PRIVATE);
                                        fos.write(impo.getBytes());
                                        fos.close();
                                    }
                                } catch(FileNotFoundException e){
                                    e.printStackTrace();
                                } catch(IOException e){
                                    e.printStackTrace();
                                }
                                // ????????????
                                String addstr = "";

                                Data data = new Data(strTitle, strContent);
                                listData.set(getAbsoluteAdapterPosition(), data);
                                for (int i = 0; i<listData.size(); i++){
                                    addstr += getItemopsition(i) + "," + getContentopsition(i) + ",";
                                    //addstr??? ??????
                                }
                                if(addstr != ""){
                                    addstr = addstr.replaceAll(",$", "");
                                }
                                // ???????????? ???????????? ?????? ??????.
//                                Log.w("addstr", addstr);
                                filesave(context, "datafile.txt", addstr);
                                // addstr ????????? ?????? ??????(????????????)
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;
                    case 1002: // ?????? ??????
                        String addstr = ""; // text??? ??????????????? ?????? ?????????.
                        // listdata ???????????? ??????????????? ??????. ???????????? context????????? ????????? ?????? ??????.(????????? ??????)
                        Log.w("??????", String.valueOf(listData.size()));
//                        Log.w("??????", String.valueOf(getAbsoluteAdapterPosition()));
                        if(getAbsoluteAdapterPosition() == 0 && listData.size() > 2){
                            Log.w("test1", "test1");
                            Data data = new Data(listData.get(1).getTitle(), listData.get(1).getContent());
                            Log.w("??????", listData.get(1).getTitle());
                            listData.set(0, data);
                            notifyDataSetChanged();
                            listData.remove(1);
                            notifyItemChanged(1);
                            notifyItemRangeChanged(1, listData.size());
                        }
                        else{
                            Log.w("test", "test");
                            listData.remove(getAdapterPosition());
                            notifyItemChanged(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), listData.size());
                        }
                        // ?????????????????? ???????????? ( ???????????? ???????????? )
                        for (int i = 0; i<listData.size(); i++){
                            addstr += getItemopsition(i) + "," + getContentopsition(i) + ",";
                        }
                        if(addstr != ""){
                            addstr = addstr.replaceAll(",$", "");
                            addstr = addstr.replace("#", "");
                        }
                        if(listData.isEmpty()){
                            Log.w("?????????", "?????????");
                            try{
                                File f = new File("/data/data/com.example.myRefri02/files/datafile.txt");
                                if(f.exists()){
                                    f.delete();
                                    Log.w("??????","??????");
                                    return true;
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        // ?????? ??????.
//                        Log.w("addstr", addstr);
                        filesave(context, "datafile.txt", addstr);
                        break;
                }
                return true;
            }
        };
    }

    public Editor_Adapter(Context acontext){
        context = acontext;
    }

}

