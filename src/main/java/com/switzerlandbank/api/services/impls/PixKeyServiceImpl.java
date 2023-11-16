package com.switzerlandbank.api.services.impls;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.switzerlandbank.api.entities.Account;
import com.switzerlandbank.api.entities.PixKey;
import com.switzerlandbank.api.entities.enums.KeyType;
import com.switzerlandbank.api.repositories.AccountRepository;
import com.switzerlandbank.api.repositories.PixKeyRepository;
import com.switzerlandbank.api.services.PixKeyService;
import com.switzerlandbank.api.services.exceptions.ResourceNotFoundException;

@Service
public class PixKeyServiceImpl implements PixKeyService {
	
	@Autowired
	private PixKeyRepository pixKeyRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public List<PixKey> findAll() {
		return pixKeyRepository.findAll();
	}

	@Override
	public List<PixKey> findByAccountId(Long accountId) {
		return pixKeyRepository.findByAccountId(accountId);
	}

	@Override
	public PixKey findById(Long id) {
		Optional<PixKey> obj = pixKeyRepository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	@Override
	public PixKey insert(PixKey obj) {
		validateKeyType(obj);
		return pixKeyRepository.save(obj);
	}
	
	@Override
	public void delete(Long id) {
		if (!pixKeyRepository.existsById(id)) {
			throw new ResourceNotFoundException(id);
		}
		pixKeyRepository.deleteById(id);
	}
	
	private void validateKeyType(PixKey obj) {
		Account account = accountRepository.getReferenceById(obj.getAccount().getId());
		if (obj.getKeyType() == KeyType.valueOf(1)) {
			obj.setKeyValue(account.getClient().getCpf());
		} else if (obj.getKeyType() == KeyType.valueOf(2)) {
			obj.setKeyValue(account.getClient().getEmail());
		} else if (obj.getKeyType() == KeyType.valueOf(3)) {
			obj.setKeyValue(generateRandomKey());
		}
	}
	
	private String generateRandomKey() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

}
