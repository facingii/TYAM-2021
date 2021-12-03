package com.outlook.gonzasosa.architecturecomponents.models;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public class DataModel {

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> lastName = new ObservableField<>();
    public final ObservableInt age = new ObservableInt();
    public final ObservableField<String> address = new ObservableField<>();

}
