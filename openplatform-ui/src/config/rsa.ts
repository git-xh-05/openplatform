/**
 * RSA 加密配置
 * 后端接口密码等敏感字段使用 RSA 公钥加密传输
 * 对应私钥配置在后端 application.yml 的 continew-starter.encrypt.field.private-key（尚未配置，待后端处理）
 */
export const RSA_PUBLIC_KEY = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj2sBtxSn6WG7G3UFiVzR
1zgCeyO/hNBH+hurzxWzWTgyq8l2Ac4dimSzG65k4LogNp6jcbc5vbH6C/ap9Sxi
1cjKhhqtZx+Z39zGrIp0CGdoxCxY2tkMAkLyHJ/GWfdG0gO3+jtGtrOb5f7oCNqg
4fC5R6Bffleym9xiizFL2p6k85S9/CMH30efy7Xf8CTAu6bsckNzhMmFIGavpv8r
Ud2XSHZb273Mt9B401EAgk4uOAW2puFCkcYZO4hzO0+YQwHa+N0eop5fSmwprDd3
Cdks6pBeO7qPlCwqGXkvrIWlthwDVtF35f6L2UTEjqN42CwBYQnoygdVGm2BWtTZ
9QIDAQAB
-----END PUBLIC KEY-----`;