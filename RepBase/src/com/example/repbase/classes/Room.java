package com.example.repbase.classes;

import java.util.ArrayList;
import java.util.List;

public class Room {
	private int id = 0;
	private String name;
	private int baseId;
	private int square;
	private boolean deleted;
	private String description;
	private List<Integer> conditionIds = new ArrayList<Integer>();
	
	public Room(){
		
	}
	
	public Room(int id, String name, int baseId, int square, boolean deleted,
			String description, List<Integer> conditionsIds) {
		this.id = id;
		this.name = name;
		this.baseId = baseId;
		this.square = square;
		this.deleted = deleted;
		this.description = description;
		this.conditionIds = conditionsIds;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	protected void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the baseId
	 */
	public int getBaseId() {
		return baseId;
	}
	/**
	 * @param baseId the baseId to set
	 */
	protected void setBaseId(int baseId) {
		this.baseId = baseId;
	}
	/**
	 * @return the square
	 */
	public int getSquare() {
		return square;
	}
	/**
	 * @param square the square to set
	 */
	protected void setSquare(int square) {
		this.square = square;
	}
	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	protected void markAsDeleted() {
		this.deleted = true;
	}
	
	protected void unMarkAsDeleted() {
		this.deleted = false;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	protected void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the conditionIds
	 */
	public List<Integer> getConditionIds() {
		return conditionIds;
	}
	/**
	 * @param conditionIds the conditionIds to set
	 */
	protected void setConditionIds(List<Integer> conditionIds) {
		this.conditionIds = conditionIds;
	}
	
	public String toStringFullInfo() {
		String strCommonInfo = "id: " + this.id 
				+ ", name: " + this.name
				+ ", baseId: " + this.baseId
				+ ", square: " + this.square
				+ ", deleted: " + this.deleted
				+ ", descriptiond: " + this.description;
		StringBuilder sbConditions = new StringBuilder();
		sbConditions.append("rooms: [");
		for (Integer i : conditionIds)
		{
			sbConditions.append(i);
			sbConditions.append("; ");
		}
		sbConditions.append("]");

		return strCommonInfo+", "+sbConditions;
	}
}
