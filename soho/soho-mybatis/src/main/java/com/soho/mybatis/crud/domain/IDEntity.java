package com.soho.mybatis.crud.domain;

import java.io.Serializable;

public interface IDEntity<ID> extends Serializable {

	public ID getId();

	public void setId(ID id);

}
