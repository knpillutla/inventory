package com.example.inventory.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{
	@Query("select i from Inventory i where i.locnNbr=:locnNbr and i.id=:id")
	public Inventory findById(@Param("locnNbr") Integer locnNbr, @Param("id") Long inventoryId);

	@Query("select i from Inventory i where i.busName=:busName and i.locnNbr=:locnNbr and i.itemBrcd=:itemBrcd and stat_code=:statCode")
	public List<Inventory> findByBusNameAndLocnNbrAndItemBrcd(@Param("busName") String busName,@Param("locnNbr") Integer locnNbr, @Param("itemBrcd") String itemBrcd,  @Param("statCode") Integer statCode);

	@Query("select i from Inventory i where i.locnNbr=:locnNbr and i.ilpn=:ilpn and stat_code=:statCode")
	public List<Inventory> findByIlpn(@Param("locnNbr") Integer locnNbr, @Param("ilpn") String ilpn,  @Param("statCode") Integer statCode);
}
