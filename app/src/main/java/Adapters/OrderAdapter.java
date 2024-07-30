package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ordernow.R;
import Models.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderTitle.setText(order.getTitle());
        holder.orderDescription.setText(order.getDescription());

        boolean isExpanded = order.isExpanded();
        holder.orderDescription.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.toggleButton.setText(isExpanded ? "Hide Details" : "Show Details");

        holder.toggleButton.setOnClickListener(v -> {
            order.setExpanded(!order.isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView orderTitle;
        public TextView orderDescription;
        public Button toggleButton;

        public OrderViewHolder(View view) {
            super(view);
            orderTitle = view.findViewById(R.id.order_title);
            orderDescription = view.findViewById(R.id.order_description);
            toggleButton = view.findViewById(R.id.toggle_button);
        }
    }
}
