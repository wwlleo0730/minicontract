package cn.iclass.udap.minicontract.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class IdEntity implements Serializable {

	private static final long serialVersionUID = 8047894099937923314L;

    @Id
    @GeneratedValue
	private Long id;// 主键

	@Version
	public long version;

	public Long createTime = System.currentTimeMillis();

	public Long lastModifyTime = System.currentTimeMillis();

}
