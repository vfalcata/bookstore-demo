package data.query;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import data.schema.DataSchema;

public abstract class NumberQuery<T extends NumberQuery,U extends AttributeAccess> extends Query<T,U> implements NumberQueryable<T>{

	protected NumberQuery(Query query, DataSchema dataSchema) {
		super(query, dataSchema);
	}
	
	@Override
	public T numberAtMost(String max) {
		this.addDataAccessString(new DataAccessString.Builder()
				.withTableName(this.dataSchema.tableName())
				.withReferenceOperator(this.referenceOperator)
				.withAttributeName(this.currentAttributeAccess)
				.withDataAccessParameterPrefix(" <= ")
				.withDataAccessParameterSuffix("")
				.withDataAccessParameter(max)
				.build()
				);
		return (T) this;
	}
	@Override
	public T numberAtLeast(String min) {
		this.addDataAccessString(new DataAccessString.Builder()
				.withTableName(this.dataSchema.tableName())
				.withReferenceOperator(this.referenceOperator)
				.withAttributeName(this.currentAttributeAccess)
				.withDataAccessParameterPrefix(" >= ")
				.withDataAccessParameterSuffix("")
				.withDataAccessParameter(min)
				.build());
		return (T) this;
	}
	@Override
	public T numberEquals(String number) {
		this.addDataAccessString(new DataAccessString.Builder()
				.withTableName(this.dataSchema.tableName())
				.withReferenceOperator(this.referenceOperator)
				.withAttributeName(this.currentAttributeAccess)
				.withDataAccessParameterPrefix(" = ")
				.withDataAccessParameterSuffix("")
				.withDataAccessParameter(number)
				.build()
				);
		return (T) this;
	}
	@Override
	public T numberBetween(String min,String max) {
		numberAtLeast(min);
		numberAtMost(max);
		return (T) this;
	}

}
