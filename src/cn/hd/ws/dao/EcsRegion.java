package cn.hd.ws.dao;

public class EcsRegion {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ecs_region.region_id
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    private Short regionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ecs_region.parent_id
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    private Short parentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ecs_region.region_name
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    private String regionName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ecs_region.region_type
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    private Boolean regionType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ecs_region.agency_id
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    private Short agencyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ecs_region.freight
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    private Integer freight;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ecs_region.region_id
     *
     * @return the value of ecs_region.region_id
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public Short getRegionId() {
        return regionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ecs_region.region_id
     *
     * @param regionId the value for ecs_region.region_id
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public void setRegionId(Short regionId) {
        this.regionId = regionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ecs_region.parent_id
     *
     * @return the value of ecs_region.parent_id
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public Short getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ecs_region.parent_id
     *
     * @param parentId the value for ecs_region.parent_id
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public void setParentId(Short parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ecs_region.region_name
     *
     * @return the value of ecs_region.region_name
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ecs_region.region_name
     *
     * @param regionName the value for ecs_region.region_name
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ecs_region.region_type
     *
     * @return the value of ecs_region.region_type
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public Boolean getRegionType() {
        return regionType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ecs_region.region_type
     *
     * @param regionType the value for ecs_region.region_type
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public void setRegionType(Boolean regionType) {
        this.regionType = regionType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ecs_region.agency_id
     *
     * @return the value of ecs_region.agency_id
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public Short getAgencyId() {
        return agencyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ecs_region.agency_id
     *
     * @param agencyId the value for ecs_region.agency_id
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public void setAgencyId(Short agencyId) {
        this.agencyId = agencyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ecs_region.freight
     *
     * @return the value of ecs_region.freight
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public Integer getFreight() {
        return freight;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ecs_region.freight
     *
     * @param freight the value for ecs_region.freight
     *
     * @mbggenerated Thu Jul 23 11:19:15 CST 2015
     */
    public void setFreight(Integer freight) {
        this.freight = freight;
    }
}