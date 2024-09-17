package com.nespresso.user.repository;

import com.nespresso.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository  extends JpaRepository<Address, UUID> {
}
