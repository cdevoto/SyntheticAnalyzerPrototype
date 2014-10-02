package com.compuware.apm.ruxit.synth.analyzer.model;


public class TupleImpl extends AbstractTuple {
	
	public static Builder newTuple (Attributes schema) {
		return new Builder(schema);
	}
	
	private TupleImpl (Builder builder) {
		super(builder);
	}

	@Override
	protected Tuple.Builder newTupleBuilder(
			Attributes attributes) {
		return newTuple(attributes);
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(this.getClass().getSimpleName() + " [");
		boolean firstAttribute = true;
		for (Attribute<?> attribute : getAttributes()) {
			if (firstAttribute) {
				firstAttribute = false;
			} else {
			    buf.append(", ");
			}
			buf.append(attribute.getName()).append("=").append(getValues().get(attribute));
		}
	    buf.append("]");
	    return buf.toString();
	}

	public static class Builder extends AbstractTuple.Builder {

		private Builder(Attributes schema) {
			super(schema);
		}

		@Override
		protected Tuple doBuild() {
			return new TupleImpl(this);
		}
		
	}

}
