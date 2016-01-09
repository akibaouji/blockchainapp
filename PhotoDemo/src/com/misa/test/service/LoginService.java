package com.misa.test.service;

import com.misa.framework.exception.ApplicationException;
import com.misa.framework.exception.DBException;
import com.misa.framework.exception.RemoteServiceNotFoundException;
import com.misa.framework.rpc.ClientAttribute;
import com.misa.framework.rpc.IRequestBuilder;
import com.misa.framework.rpc.RemoteCallTemplate;
import com.misa.framework.rpc.RemoteRequest;
import com.misa.test.app.PhotoDemoApplication;
import com.misa.test.dxo.LoginDxo;
import com.misa.test.entity.LoginUpEntity;
import com.misa.test.entity.UserEntity;

/**
 * 演示了远程服务调用
 * 
 * @author wangyilin
 *
 */
public class LoginService 
{
	public UserEntity login(LoginUpEntity upEntity) 
			throws ApplicationException, DBException
	{
		RemoteCallTemplate<LoginUpEntity, UserEntity> template
			= new RemoteCallTemplate<LoginUpEntity, UserEntity>();

		return template.getOne(new IRequestBuilder(){
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
		}, new LoginDxo(), (short)0x0001, upEntity);
	}
}
