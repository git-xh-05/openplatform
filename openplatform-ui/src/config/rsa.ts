/**
 * RSA 加密配置
 * 后端接口密码等敏感字段使用 RSA 公钥加密传输
 * 对应私钥配置在后端 application.yml 的 continew-starter.encrypt.field.private-key（尚未配置，待后端处理）
 */
export const RSA_PUBLIC_KEY = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvE4m+Es7kW9vOT3MQ1dQ
OqdjtST22o1PXSpxus0UfPqaR8ylgf4FztCeTfw2Ogz/pWh17hs7nnU9O4SgluBk
5Nt1sMdTJorGOP0CDQFTeqdZ6jg+IxALQoc9cOSUqhkkP1T00WicpAOU/v3zC7r+
oh9/Hfo7ykYl0EkjdNSzslhr2z08ci+xRCL3TSPEE3W45r+2oZnJdqDKbzPvgJJ8
uxTaR/tQQNO65WEawJaHDP08ZgqbgAnj4CS1xu+d0RC2ql2ku2b7bM7lGW52xx5w
twsIjeS9auBGdFD9X6hQUC1nCLy8+cjgtMFFAuNerZAo1XMz+qXigy6Zm/Jd/Bq+
9wIDAQAB
-----END PUBLIC KEY-----`;