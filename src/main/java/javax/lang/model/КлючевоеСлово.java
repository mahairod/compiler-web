/*
 * Авторское право принадлежит ООО «Эллиптика» ѱ 2018.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Elliptica Ltd. All rights reserved.
 * 
 *  Частная лицензия Эллиптика
 * Данный программный код является собственностью ООО «Эллиптика»
 * и может быть использован только с его разрешения
 */
package javax.lang.model;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public enum КлючевоеСлово {
	ABSTRACT("абстрактный"), ASSERT("проверь"), BOOLEAN("логическое"), BREAK("прерви"), BYTE("байт"), CASE("случай"), CATCH("ловя"), CHAR("символ"), CLASS("класс"), CONST("неизм"), CONTINUE("возобнови"), DEFAULT("запасной"), DO("делай"), DOUBLE("ширдроб"), ELSE("иначе"), ENUM("переч"), EXTENDS("расширяет"), FINAL("итоговый"), FINALLY("напоследок"), FLOAT("дроб"), FOR("для"), GOTO("идик"), IF("если"), IMPLEMENTS("воплощает"), IMPORT("внеси"), INSTANCEOF("экземпляр"), INT("цел"), INTERFACE("сопряжение"), LONG("ширцел"), NATIVE("туземный"), NEW("новый"), PACKAGE("пакет"), PRIVATE("личный"), PROTECTED("защищённый"), PUBLIC("доступный"), RETURN("верни"), SHORT("узцел"), STATIC("статичный"), STRICTFP("строгарифм"), SUPER("поверх"), SWITCH("выбери"), SYNCHRONIZED("синхронизированный"), THIS("это"), THROW("кинь"), THROWS("кидает"), TRANSIENT("мимолётный"), TRY("попробуй"), VOID("тщетный"), VOLATILE("изменчивый"), WHILE("пока"), TRUE("истина"), FALSE("ложь"), NULL("ничто");
	public final String значение;

	КлючевоеСлово(String значение) {
		this.значение = значение;
	}
	
}
