package org.zoltor.model.entities;

import org.zoltor.common.HelperUtils;

import java.util.Date;

/**
 * Created by zoltor on 31.10.14.
 */
public class ResultEntity {
    private long id;
    private FormulaEntity formula;
    private Date resultRegistered;
    private long resultTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FormulaEntity getFormula() {
        return formula;
    }

    public void setFormula(FormulaEntity formula) {
        this.formula = formula;
    }

    public Date getResultRegistered() {
        return resultRegistered;
    }

    public void setResultRegistered(String resultRegistered) {
        this.resultRegistered = HelperUtils.getDateFromDb(resultRegistered);
    }

    public long getResultTime() {
        return resultTime;
    }

    public void setResultTime(long resultTime) {
        this.resultTime = resultTime;
    }
}
