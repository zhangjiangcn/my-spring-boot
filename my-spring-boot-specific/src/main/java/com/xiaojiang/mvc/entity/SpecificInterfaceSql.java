package com.xiaojiang.mvc.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZhangJiang
 * @since 2019-02-03
 */
public class SpecificInterfaceSql extends Model<SpecificInterfaceSql> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据空间
     */
    private String dataSpace;

    /**
     * 数据sql
     */
    private String dataSql;

    /**
     * 数据参数Id
     */
    private String dataParamId;

    /**
     * 数据源Id
     */
    private Integer dataSourceId;

    /**
     * sql模板引擎 :
  null || mybatis : mybatis模板引擎；
  beetl : beetl模板引擎。
     */
    private String sqlTemplateEngine;

    /**
     * 结果数据格式 
  array：数组；
  object：对象。
     */
    private String resultDataFormat;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标记 0.未删除；1.已删除
     */
    @TableLogic
    private Integer deleteFlag;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataSpace() {
        return dataSpace;
    }

    public void setDataSpace(String dataSpace) {
        this.dataSpace = dataSpace;
    }

    public String getDataSql() {
        return dataSql;
    }

    public void setDataSql(String dataSql) {
        this.dataSql = dataSql;
    }

    public String getDataParamId() {
        return dataParamId;
    }

    public void setDataParamId(String dataParamId) {
        this.dataParamId = dataParamId;
    }

    public Integer getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getSqlTemplateEngine() {
        return sqlTemplateEngine;
    }

    public void setSqlTemplateEngine(String sqlTemplateEngine) {
        this.sqlTemplateEngine = sqlTemplateEngine;
    }

    public String getResultDataFormat() {
        return resultDataFormat;
    }

    public void setResultDataFormat(String resultDataFormat) {
        this.resultDataFormat = resultDataFormat;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SpecificInterfaceSql{" +
        "id=" + id +
        ", dataType=" + dataType +
        ", dataSpace=" + dataSpace +
        ", dataSql=" + dataSql +
        ", dataParamId=" + dataParamId +
        ", dataSourceId=" + dataSourceId +
        ", sqlTemplateEngine=" + sqlTemplateEngine +
        ", resultDataFormat=" + resultDataFormat +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", remark=" + remark +
        ", deleteFlag=" + deleteFlag +
        "}";
    }
}
