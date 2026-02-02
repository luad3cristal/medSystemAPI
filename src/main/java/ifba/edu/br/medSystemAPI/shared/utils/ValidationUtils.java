package ifba.edu.br.medSystemAPI.shared.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

  // Regex para CPF (formato: 000.000.000-00 ou 00000000000)
  private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$");

  // Regex para CRM (formato: CRM/UF 000000 - exemplo: CRM/SP 123456)
  private static final Pattern CRM_PATTERN = Pattern.compile("^CRM/[A-Z]{2}\\s?\\d{4,6}$");

  // Regex para Email
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  // Regex para CEP (formato: 00000-000 ou 00000000)
  private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");

  // Regex para Telefone (formatos: (00) 00000-0000, (00) 0000-0000, 00000000000,
  // etc)
  private static final Pattern PHONE_PATTERN = Pattern.compile("^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$");

  /**
   * Valida o formato do CPF
   */
  public static boolean isValidCPFFormat(String cpf) {
    if (cpf == null || cpf.trim().isEmpty()) {
      return false;
    }
    return CPF_PATTERN.matcher(cpf).matches();
  }

  /**
   * Valida o CPF completo (formato + dígitos verificadores)
   */
  public static boolean isValidCPF(String cpf) {
    if (!isValidCPFFormat(cpf)) {
      return false;
    }

    // Remove pontos e traços
    String cleanCPF = cpf.replaceAll("[.\\-]", "");

    // Verifica se tem 11 dígitos
    if (cleanCPF.length() != 11) {
      return false;
    }

    // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
    if (cleanCPF.matches("(\\d)\\1{10}")) {
      return false;
    }

    // Validação dos dígitos verificadores
    try {
      int[] digits = cleanCPF.chars().map(c -> c - '0').toArray();

      // Primeiro dígito verificador
      int sum = 0;
      for (int i = 0; i < 9; i++) {
        sum += digits[i] * (10 - i);
      }
      int firstDigit = 11 - (sum % 11);
      if (firstDigit >= 10)
        firstDigit = 0;

      if (digits[9] != firstDigit) {
        return false;
      }

      // Segundo dígito verificador
      sum = 0;
      for (int i = 0; i < 10; i++) {
        sum += digits[i] * (11 - i);
      }
      int secondDigit = 11 - (sum % 11);
      if (secondDigit >= 10)
        secondDigit = 0;

      return digits[10] == secondDigit;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Valida o formato do CRM
   */
  public static boolean isValidCRMFormat(String crm) {
    if (crm == null || crm.trim().isEmpty()) {
      return false;
    }
    return CRM_PATTERN.matcher(crm.toUpperCase()).matches();
  }

  /**
   * Valida o CRM completo (formato + UF válida)
   */
  public static boolean isValidCRM(String crm) {
    if (!isValidCRMFormat(crm)) {
      return false;
    }

    // Extrai a UF do CRM
    String uf = crm.substring(4, 6).toUpperCase();

    // Lista de UFs válidas no Brasil
    String[] validUFs = {
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
        "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
        "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    };

    for (String validUF : validUFs) {
      if (validUF.equals(uf)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Valida o formato do email
   */
  public static boolean isValidEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      return false;
    }
    return EMAIL_PATTERN.matcher(email).matches();
  }

  /**
   * Valida o formato do CEP
   */
  public static boolean isValidCEP(String cep) {
    if (cep == null || cep.trim().isEmpty()) {
      return false;
    }
    return CEP_PATTERN.matcher(cep).matches();
  }

  /**
   * Valida o formato do telefone
   */
  public static boolean isValidPhone(String phone) {
    if (phone == null || phone.trim().isEmpty()) {
      return false;
    }
    return PHONE_PATTERN.matcher(phone).matches();
  }

  /**
   * Remove formatação do CPF
   */
  public static String cleanCPF(String cpf) {
    return cpf == null ? null : cpf.replaceAll("[.\\-]", "");
  }

  /**
   * Remove formatação do CEP
   */
  public static String cleanCEP(String cep) {
    return cep == null ? null : cep.replaceAll("-", "");
  }

  /**
   * Remove formatação do telefone
   */
  public static String cleanPhone(String phone) {
    return phone == null ? null : phone.replaceAll("[()\\s-]", "");
  }
}
