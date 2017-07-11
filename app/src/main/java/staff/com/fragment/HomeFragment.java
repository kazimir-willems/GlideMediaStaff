package staff.com.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;
import staff.com.R;
import staff.com.application.DeliveryApplication;
import staff.com.consts.StateConsts;
import staff.com.event.DownloadStockInfoEvent;
import staff.com.event.MakeModifiedDataEvent;
import staff.com.event.MakeUploadDataEvent;
import staff.com.event.RemoveAllDataEvent;
import staff.com.event.StockInfoStoreEvent;
import staff.com.event.UploadStockInfoEvent;
import staff.com.task.DownloadStockInfoTask;
import staff.com.task.MakeModifiedDataTask;
import staff.com.task.MakeUploadDataTask;
import staff.com.task.RemoveAllDataTask;
import staff.com.task.StockInfoStoreTask;
import staff.com.task.UploadModifiedStockTask;
import staff.com.task.UploadStockInfoTask;
import staff.com.ui.MainActivity;
import staff.com.vo.DownloadStockInfoResponseVo;
import staff.com.vo.UploadStockInfoResponseVo;

public class HomeFragment extends Fragment {

    private ProgressDialog progressDialog;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDownloadStockInfoEvent(DownloadStockInfoEvent event) {
        hideProgressDialog();
        DownloadStockInfoResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            parseStockInfo(responseVo);
        } else {
            networkError();
        }
    }

    @Subscribe
    public void onUploadDespatchEvent(UploadStockInfoEvent event) {
        hideProgressDialog();
        UploadStockInfoResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            uploadSuccess();
        } else {
            networkError();
        }
    }

    @Subscribe
    public void onStockInfoStoreEvent(StockInfoStoreEvent event) {
        hideProgressDialog();
        int result = event.getResponse();
        if(result == 1) {
            downloadSuccess();
        } else if (result == 2) {
            noDespatch();
        } else if (result == 0){
            dbError();
        }
    }

    @Subscribe
    public void onRemoveDespatchesEvent(RemoveAllDataEvent event) {
        hideProgressDialog();
        boolean result = event.getRemoveResult();
        if(result) {
            Toast.makeText(getActivity(), R.string.remove_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.remove_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onMakeUploadDataEvent(MakeUploadDataEvent event) {
        hideProgressDialog();
        String result = event.getResponse();
        if(result == null || result.isEmpty()) {
            noCompletedDespatch();
        } else {
            startUploadStockInfo(result);
        }
    }

    @Subscribe
    public void onMakeModifiedDataEvent(MakeModifiedDataEvent event) {
        hideProgressDialog();
        String result = event.getResponse();
        if(result == null || result.isEmpty()) {
            noCompletedDespatch();
        } else {
            startUploadStockInfo(result);
        }
    }

    @OnClick(R.id.btn_download_stock)
    void onClickBtnDownStocks() {
        progressDialog.setMessage(getResources().getString(R.string.downloading));
        progressDialog.show();
        startDownloadStockInfo();
    }

    @OnClick(R.id.btn_upload_stock)
    void onClickBtnUploadStock() {
        progressDialog.setMessage(getResources().getString(R.string.uploading));
        progressDialog.show();

        MakeUploadDataTask task = new MakeUploadDataTask(getActivity());
        task.execute();
    }

    @OnClick(R.id.btn_upload_modified_stock)
    void onClickBtnUploadModifiedStock() {
        progressDialog.setMessage(getResources().getString(R.string.uploading));
        progressDialog.show();

        MakeModifiedDataTask task = new MakeModifiedDataTask(getActivity());
        task.execute();
    }

    @OnClick(R.id.btn_remove_stock)
    void onClickBtnRemoveStock() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_ask_remove_datas));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setMessage(getResources().getString(R.string.removing));
                progressDialog.show();
                RemoveAllDataTask task = new RemoveAllDataTask(getActivity());
                task.execute();
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void startDownloadStockInfo() {
        DownloadStockInfoTask task = new DownloadStockInfoTask();
        task.execute();
    }

    private void startUploadStockInfo(String data) {
        progressDialog.setMessage(getResources().getString(R.string.uploading));
        progressDialog.show();

        UploadStockInfoTask task = new UploadStockInfoTask();
        task.execute(data);
    }

    private void startModifiedStockInfo(String data) {
        progressDialog.setMessage(getResources().getString(R.string.uploading));
        progressDialog.show();

        UploadModifiedStockTask task = new UploadModifiedStockTask();
        task.execute(data);
    }

    private void hideProgressDialog() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void uploadSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.upload_success));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void networkError() {
        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    private void noCompletedDespatch() {
        Toast.makeText(getActivity(), R.string.no_completed_despatch, Toast.LENGTH_SHORT).show();
    }

    private void dbError() {
        Toast.makeText(getActivity(), R.string.db_error, Toast.LENGTH_SHORT).show();
    }

    private void downloadSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.download_accomplished));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity) getActivity()).showNextFragment();
            }
        }).create().show();
    }

    private void noDespatch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.no_despatch));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void parseStockInfo(DownloadStockInfoResponseVo responseVo) {
        progressDialog.setMessage(getResources().getString(R.string.processing));
        progressDialog.show();
        StockInfoStoreTask task = new StockInfoStoreTask(getActivity());
        task.execute(responseVo);
    }

    private void showPermissionDenied() {
        Toast.makeText(getActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
    }

}
