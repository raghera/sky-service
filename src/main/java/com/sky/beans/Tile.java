package com.sky.beans;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.codehaus.jackson.map.util.Comparators;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Represents a Tile entity
 * 
 * @author Ravi Aghera
 *
 */
public class Tile {

	//Constant values
	public static final String MESSAGE_POS_SIZE = "Position must be between 1 and 8 inclusive";
	public static final String MESSAGE_LABEL_SIZE = "Label cannot be blank or more than 30 characters";
	public static final String MESSAGE_LABEL_ALPHANUMERIC = "Label must contain alphamumeric characters and spaces only";
	
	@Max(value=8, message=MESSAGE_POS_SIZE) 
	@Min(value=1, message=MESSAGE_POS_SIZE)
	private int position;
	
	@NotBlank(message=MESSAGE_LABEL_SIZE)
	@Size(max=30, message=MESSAGE_LABEL_SIZE)
	@Pattern(regexp="^[a-zA-Z0-9 ]+$", message=MESSAGE_LABEL_ALPHANUMERIC)
	private String label;
	
	private String clientId;
	private Date startDate;
	private Date endDate;
	
	public Tile() {
	}
	
	public Tile(int position, String label, String clientId, Date startDate, Date endDate) {
		this.position = position;
		this.label = label;
		this.clientId = clientId;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tile [position=").append(position).append(", label=")
				.append(label).append(", clientId=").append(clientId)
				.append(", startDate=").append(startDate).append(", endDate=")
				.append(endDate).append("]");
		return builder.toString();
	}
	
}
