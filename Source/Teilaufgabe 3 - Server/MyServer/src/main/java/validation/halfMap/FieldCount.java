package validation.halfMap;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;

public class FieldCount implements HalfMapValidator {

	@Override
	public void validate(HalfMap data) {
		long countGrass = data.getNodes().stream().filter(ter -> ter.getTerrain() == ETerrain.Grass).count();
		long countWater = data.getNodes().stream().filter(ter -> ter.getTerrain() == ETerrain.Water).count();
		long countMountain = data.getNodes().stream().filter(ter -> ter.getTerrain() == ETerrain.Mountain).count();
	}

}
