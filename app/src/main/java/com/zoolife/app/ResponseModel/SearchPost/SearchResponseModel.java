package com.zoolife.app.ResponseModel.SearchPost;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SearchResponseModel{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("error")
	private boolean error;

	@SerializedName("status")
	private int status;

	public List<DataItem> getData(){
		return data;
	}

	public boolean isError(){
		return error;
	}

	public int getStatus(){
		return status;
	}
}