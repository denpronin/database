package com.pronin.mrtestingtask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pronin.mrtestingtask.database.Staff;
import java.util.ArrayList;
import java.util.List;

public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.StaffListHolder>{

    private final List<Staff> staffList = new ArrayList<>();
    private final OnStaffClickListener onstaffClickListener;

    public interface OnStaffClickListener {
        void onStaffClick(Staff staff);
    }

    public StaffListAdapter(OnStaffClickListener onStaffClickListener) {
        this.onstaffClickListener = onStaffClickListener;
    }

    @NonNull
    @Override
    public StaffListAdapter.StaffListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
        return new StaffListAdapter.StaffListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffListAdapter.StaffListHolder holder, int position) {
        holder.bind(staffList.get(position));
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public void setItems (List<Staff> argStaffList) {
        staffList.addAll(argStaffList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        staffList.clear();
        notifyDataSetChanged();
    }

    class StaffListHolder extends RecyclerView.ViewHolder {

        TextView firstName;
        TextView lastName;
        TextView position;
        TextView office;
        TextView city;

        public StaffListHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.first_name);
            lastName = itemView.findViewById(R.id.last_name);
            position = itemView.findViewById(R.id.position);
            office = itemView.findViewById(R.id.office);
            city = itemView.findViewById(R.id.city);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Staff staff = staffList.get(getLayoutPosition());
                    onstaffClickListener.onStaffClick(staff);
                }
            });
        }

        public void bind(Staff staff) {
            firstName.setText(staff.getFirstName());
            lastName.setText(staff.getLastName());
            position.setText(staff.getPosition());
            office.setText(staff.getOffice());
            city.setText(staff.getCity());
        }
    }
}
