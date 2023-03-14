package com.backspace.rdbms;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SchemaDetails {
	@Id
	private int id;
	private String kind;
	private String url;
	private String username;
	private String password;
	private String tenant;
	private String nosqlUrl;
	private String nosqlUsername;
	private String nosqlPassword;
	private String nosqlKind;

}
