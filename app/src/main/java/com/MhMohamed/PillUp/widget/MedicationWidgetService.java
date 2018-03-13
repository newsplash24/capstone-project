package com.MhMohamed.PillUp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;


/**
 * Created by Mohamed on 3/7/2018.
 */

public class MedicationWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MedicationWidgetFactory(this.getApplicationContext(), intent);
    }
}
