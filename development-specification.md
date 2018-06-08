
命名规范:
	系统:以dlxy-XXX-system 如:portal系统: dlxy-portal-systme
	包的规范:com.dlxy.system.XXX 如:com.dlxy.system.portal
	
	模块:以dlxy-XXX-server 如:login服务:dlxy-login-server
	包的规范:com.dlxy.server.xxx 如:com.dlxy.server.login
		子模块的规范:dlxy-xxx-server-xxx 如:dlxy-login-server-common
		子包的规范:com.dlxy.server.xxx.xxx: com.dlxy.server.login.dao

	对外开放服务的模块统一为:dlxy-xxx-mpi (本应该还有区分的:一个内部调用,一个只在服务间调用,1个交给外部,但是只是多模块的话直接全部内部调用即可)
	


端口的划分:
	系统:
		managemenet 8000
		portal:8001
