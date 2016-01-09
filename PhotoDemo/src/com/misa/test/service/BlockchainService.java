package com.misa.test.service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import com.misa.framework.exception.ApplicationException;
import com.misa.framework.exception.DBException;
import com.misa.framework.exception.RemoteServiceNotFoundException;
import com.misa.framework.rpc.ClientAttribute;
import com.misa.framework.rpc.IRequestBuilder;
import com.misa.framework.rpc.RemoteCallTemplate;
import com.misa.framework.rpc.RemoteRequest;
import com.misa.test.app.PhotoDemoApplication;
import com.misa.test.dxo.AnnounceDxo;
import com.misa.test.dxo.BlockchainDxo;
import com.misa.test.dxo.KeyWrapperDxo;
import com.misa.test.entity.AnnounceEntity;
import com.misa.test.entity.BlockchainDownEntity;
import com.misa.test.entity.BlockchainUpEntity;
import com.misa.test.entity.KeyWrapperEntity;
import com.misa.test.utility.RSAUtility;

public class BlockchainService 
{
/*	public void upload(String hashcode) throws ApplicationException, DBException
	{
		RemoteCallTemplate<BlockchainUpEntity, BlockchainDownEntity> template
		= new RemoteCallTemplate<BlockchainUpEntity, BlockchainDownEntity>();
		
		BlockchainUpEntity upEntity = new BlockchainUpEntity();
		upEntity.setHashcode(hashcode);
		upEntity.setOpType("upload");
		
		template.getOne(new IRequestBuilder(){
			@Override
			public RemoteRequest buildRequest(short serviceId, byte[] upData)
					throws RemoteServiceNotFoundException {
				// TODO Auto-generated method stub
				RemoteRequest request = new RemoteRequest(PhotoDemoApplication.address,PhotoDemoApplication.port,(byte)0x0B,(byte)0x01,serviceId,upData);
				ClientAttribute clientAttr = request.getCommonProtocolObj().getClientAttr();
				clientAttr.setIfUseChecksum(true);
				clientAttr.setIfUseEncryption(true);
				return request;
			}
		}, new BlockchainDxo(), (short)0x0002, upEntity);
	}*/
	
	/**
	 * 声明票据
	 * @param keyIndex
	 * @param hashcode
	 * @throws ApplicationException
	 * @throws DBException
	 */
	public void announce(int keyIndex, String hashcode) throws ApplicationException, DBException
	{
		RemoteCallTemplate<BlockchainUpEntity, BlockchainDownEntity> template
		= new RemoteCallTemplate<BlockchainUpEntity, BlockchainDownEntity>();
		
		BlockchainUpEntity upEntity = new BlockchainUpEntity();
		upEntity.setOpType("announce");
		upEntity.setPosId("pos1");
		upEntity.setPhoneId(PhotoDemoApplication.phoneId);
		upEntity.setKeyId(String.valueOf(keyIndex));
		upEntity.setHashcode(hashcode);
		
		//获取私钥：因为DEMO，从服务器获取，实际应该从图片中获取
		RSAPrivateKey privateKey = getPrivateKey(keyIndex);
		try {
			upEntity.setEncryptedData(RSAUtility.encryptByPrivateKey(PhotoDemoApplication.phoneId, privateKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		template.getOne(new IRequestBuilder(){
			@Override
			public RemoteRequest buildRequest(short serviceId, byte[] upData)
					throws RemoteServiceNotFoundException {
				// TODO Auto-generated method stub
				RemoteRequest request = new RemoteRequest(PhotoDemoApplication.address,PhotoDemoApplication.port,(byte)0x0B,(byte)0x01,serviceId,upData);
				ClientAttribute clientAttr = request.getCommonProtocolObj().getClientAttr();
				clientAttr.setIfUseChecksum(true);
				clientAttr.setIfUseEncryption(false);
				return request;
			}
		}, new BlockchainDxo(), (short)0x0002, upEntity);
	}
	
	/**
	 * 验证票据
	 * @param hashcode
	 * @throws ApplicationException
	 * @throws DBException
	 */
	public void verify(String hashcode) throws ApplicationException, DBException
	{
		RemoteCallTemplate<BlockchainUpEntity, BlockchainDownEntity> template
		= new RemoteCallTemplate<BlockchainUpEntity, BlockchainDownEntity>();
		
		BlockchainUpEntity upEntity = new BlockchainUpEntity();
		upEntity.setPhoneId(PhotoDemoApplication.phoneId);
		upEntity.setHashcode(hashcode);
		upEntity.setOpType("verify");
		
		template.getOne(new IRequestBuilder(){
			@Override
			public RemoteRequest buildRequest(short serviceId, byte[] upData)
					throws RemoteServiceNotFoundException {
				// TODO Auto-generated method stub
				RemoteRequest request = new RemoteRequest(PhotoDemoApplication.address,PhotoDemoApplication.port,(byte)0x0B,(byte)0x01,serviceId,upData);
				ClientAttribute clientAttr = request.getCommonProtocolObj().getClientAttr();
				clientAttr.setIfUseChecksum(true);
				clientAttr.setIfUseEncryption(false);
				return request;
			}
		}, new BlockchainDxo(), (short)0x0002, upEntity);
	}
	
	/**
	 * 获取所有公钥
	 * @return
	 * @throws ApplicationException
	 * @throws DBException
	 */
	public List<RSAPublicKey> getAllPublicKey() throws ApplicationException, DBException
	{
		RemoteCallTemplate<BlockchainUpEntity, KeyWrapperEntity> template
		= new RemoteCallTemplate<BlockchainUpEntity, KeyWrapperEntity>();
		
		BlockchainUpEntity upEntity = new BlockchainUpEntity();
		List<KeyWrapperEntity> rtn = template.getList(new IRequestBuilder(){
			@Override
			public RemoteRequest buildRequest(short serviceId, byte[] upData)
					throws RemoteServiceNotFoundException {
				RemoteRequest request = new RemoteRequest(PhotoDemoApplication.address,PhotoDemoApplication.port,(byte)0x0B,(byte)0x01,serviceId,upData);
				ClientAttribute clientAttr = request.getCommonProtocolObj().getClientAttr();
				clientAttr.setIfUseChecksum(true);
				clientAttr.setIfUseEncryption(false);
				return request;
			}
		}, new KeyWrapperDxo(), (short)0x0003, upEntity);
		
		List<RSAPublicKey> lst = new ArrayList<RSAPublicKey>();
		for (KeyWrapperEntity entity : rtn)
		{
			RSAPublicKey key = RSAUtility.getPublicKey(entity.getModulus(), entity.getExponent());
			lst.add(key);
		}
		
		return lst;
	}
	
	/**
	 * 获取私钥
	 * @param index
	 * @return
	 * @throws ApplicationException
	 * @throws DBException
	 */
	public RSAPrivateKey getPrivateKey(int index) throws ApplicationException, DBException
	{
		RemoteCallTemplate<BlockchainUpEntity, KeyWrapperEntity> template
		= new RemoteCallTemplate<BlockchainUpEntity, KeyWrapperEntity>();
		
		BlockchainUpEntity upEntity = new BlockchainUpEntity();
		upEntity.setKeyId(String.valueOf(index));
		KeyWrapperEntity rtn = template.getOne(new IRequestBuilder(){
			@Override
			public RemoteRequest buildRequest(short serviceId, byte[] upData)
					throws RemoteServiceNotFoundException {
				RemoteRequest request = new RemoteRequest(PhotoDemoApplication.address,PhotoDemoApplication.port,(byte)0x0B,(byte)0x01,serviceId,upData);
				ClientAttribute clientAttr = request.getCommonProtocolObj().getClientAttr();
				clientAttr.setIfUseChecksum(true);
				clientAttr.setIfUseEncryption(false);
				return request;
			}
		}, new KeyWrapperDxo(), (short)0x0004, upEntity);
		
		return RSAUtility.getPrivateKey(rtn.getModulus(), rtn.getExponent());
	}
	
	/**
	 * 获取所有需要验证的票据
	 * @return
	 * @throws ApplicationException
	 * @throws DBException
	 */
	public List<AnnounceEntity> getAllTobeVerifiedAnnounce() throws ApplicationException, DBException
	{
		RemoteCallTemplate<BlockchainUpEntity, AnnounceEntity> template
		= new RemoteCallTemplate<BlockchainUpEntity, AnnounceEntity>();
		
		BlockchainUpEntity upEntity = new BlockchainUpEntity();
		upEntity.setPhoneId(PhotoDemoApplication.phoneId);
		
		return template.getList(new IRequestBuilder(){
			@Override
			public RemoteRequest buildRequest(short serviceId, byte[] upData)
					throws RemoteServiceNotFoundException {
				RemoteRequest request = new RemoteRequest(PhotoDemoApplication.address,PhotoDemoApplication.port,(byte)0x0B,(byte)0x01,serviceId,upData);
				ClientAttribute clientAttr = request.getCommonProtocolObj().getClientAttr();
				clientAttr.setIfUseChecksum(true);
				clientAttr.setIfUseEncryption(false);
				return request;
			}
		}, new AnnounceDxo(), (short)0x0005, upEntity);
		
	}
	
}
