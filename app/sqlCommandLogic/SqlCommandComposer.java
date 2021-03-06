package sqlCommandLogic;

import constantField.DatabaseColumnNameVariableTable;
import databaseUtil.SqlObject;
import extractContent.ExtractArticleContentColumnObject;
import json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by roye on 2017/4/26.
 */
public class SqlCommandComposer {
    public String getUserDataSqlByIdAndSystemType(int id, int systemType) {
        String sql = "select * from articles_content as a,users_information as b,articles_information as c,class_information as d,users_special_experience as e where a.ID=b.ID and a.system_type=b.system_type and b.ID=c.ID and b.system_type=c.system_type and c.ID=d.ID and c.system_type=d.system_type and d.ID=e.ID and d.system_type=e.system_type and a.ID=" + id + " and a.system_type=" + systemType;
        return sql;
    }
    public String getOtherColumnSqlByText(String text) {
        String sql = "select * from words_table as a where a.text='" + text + "'";
        return sql;
    }
    public String getOtherColumnSqlByTextAndPOS(String text, String pos) {
        String sql = "select * from words_table as a where a.text='" + text + "' and a.pos like '" + pos + "=%'";
        return sql;
    }
    public String getOtherColumnSqlByTextAndPOSFuzzy(String text, String pos) {
        String sql = "select * from words_table as a where a.text='" + text + "' and a.pos like '%=" + pos + "'";
        return sql;
    }
    public String getOriginalSqlByWordId(int id) {
        String sql = "select sentence_id, position from original_words_Index_table as a where a.word_id='" + id + "'";
        return sql;
    }
    public String getCorrectSqlByWordId(int id) {
        String sql = "select sentence_id, position from corrected_words_Index_table as a where a.word_id='" + id + "'";
        return sql;
    }
    public String getOriginalSqlByWordId(int sentenceID, int position) {
        String sql = "select word_id from original_words_Index_table as a where a.sentence_id='"
                + sentenceID + "'" + "and a.position='" + position + "'";
        return sql;
    }
    public String getCorrectSqlByWordId(int sentenceID, int position) {
        String sql = "select word_id from corrected_words_Index_table as a where a.sentence_id='"
                + sentenceID + "'" + "and a.position='" + position + "'";
        return sql;
    }
    public String getOtherColumnSqlByNextWordIDAndPOS(int nextWordID, String pos) {
        String sql = "select * from words_table as a where a.ID='"
                + nextWordID + "'" + "and a.pos like '" + pos + "=%'";
        return sql;
    }
    public String getOriginalSqlBySentenceId(int id, int systemType) {
        String sql = "select * from original_sentences_content as a where a.ID='" + id + "' and system_type = '" + systemType + "'";
        return sql;
    }
    public String getCorrectSqlBySentenceID(int id, int systemType) {
        String sql = "select * from corrected_sentences_content as a where a.ID='" + id + "' and system_type = '" + systemType + "'";
        return sql;
    }
    public String getXMLByArticleID(String id, int systemType) {
        String sql = "select xml_content from articles_content where ID='" + id + "' and system_type = '" + systemType + "'";
        return sql;
    }
    public String getOriginalSentenceIDByArticleID(String articleID, int systemType) {
        String sql = "select min(b.ID) from original_sentences_content as b where b.article_id ='" + articleID + "' and system_type = '" + systemType + "'";
        return sql;
    }
    public String getCorrectSentenceIDByArticleID(String articleID, int systemType) {
        String sql = "select min(a.ID) from corrected_sentences_content as a where a.article_id ='" + articleID + "' and system_type = '" + systemType + "'";
        return sql;
    }
    public String getAuthorInformation(String articleID, int systemType) {
        String sql = "select a.ID,a.gender,a.school_name,a.department,b.submitted_year,a.learning_hours, c.special_experience from users_information as a, articles_information as b,users_special_experience as c " +
                "where a.ID ='" + articleID + "' and a.ID = b.ID and b.ID = c.ID and a.system_type = '" + systemType + "' and a.system_type = b.system_type and b.system_type = c.system_type";
        return sql;
    }
    public String getExistByOtherColumnCondition(Object object) {
        List<String> condition = (List<String>) object;
        String sql = "select * from users_information as a, users_special_experience as b, articles_information as c " +
                "where a.ID = '" + condition.get(0) + "' and a.ID = b.ID and b.ID = c.ID and a.learning_hours BETWEEN '" +
                condition.get(1).split("~")[0] + "' and '" + condition.get(1).split("~")[1] + "' and a.gender LIKE '%" +
                condition.get(2) + "%' and a.department LIKE '%" +
                condition.get(3) + "%' and b.special_experience BETWEEN '" +
                condition.get(4).split("~")[0] + "' and '" + condition.get(4).split("~")[1] + "'and c.number_of_words BETWEEN '" +
                condition.get(5).split("~")[0] + "' and '" + condition.get(5).split("~")[1] + "'and c.article_style BETWEEN " +
                condition.get(6).split("~")[0] + " and '" + condition.get(6).split("~")[1] + "'and c.article_topic BETWEEN " +
                condition.get(7).split("~")[0] + " and '" + condition.get(7).split("~")[1] + "'and c.writting_location BETWEEN '" +
                condition.get(8).split("~")[0] + "' and '" + condition.get(8).split("~")[1] + "'and (c.submitted_year LIKE '%" +
                condition.get(9).split("~")[0] + "%' OR " + "c.submitted_year LIKE '%" + condition.get(9).split("~")[1] + "%' OR " +
                "c.submitted_year BETWEEN '" + condition.get(9).split("~")[0] + "' and '" + condition.get(9).split("~")[1] + "')";
        return sql;
    }
    public String getTextOfLemma(String lemma) {
        String sql = "SELECT text from `words_table` WHERE lemma = '" + lemma + "'";
        return sql;
    }
    public String getTextAndPOSOfFuzzy(String fuzzy) {
        String sql = "SELECT text, pos from `words_table` WHERE text like '%" + fuzzy + "' and " +
                "(pos like '%=VEger' or pos like '%=VEinf' or pos like '%=VHger' or pos like '%=VHinf'" +
                " or pos like '%=VLger' or pos like '%=VLinf' or pos like '%=VMger' or pos like '%=VMinf' " +
                "or pos like '%=VSger' or pos like '%=VSinf')";
        return sql;
    }
    public UserData getUserData(JSONObject userDataJsonObject) {
        String sql="";
        UserData userData=new UserData();
        Set<String> columnNameSet = userDataJsonObject.keySet();
        SqlObject userInformationSqlObject=new SqlObject();
        SqlObject articleInformationSqlObject=new SqlObject();
        SqlObject classInformationSqlObject=new SqlObject();
        SqlObject userSpecialExperienceSqlObject=new SqlObject();
        SqlObject articleContentSqlObject=new SqlObject();
        ExtractArticleContentColumnObject extractArticleContentColumnObject = new ExtractArticleContentColumnObject();
        for(String columnName : columnNameSet)
        {
            if(checkTableNumber(columnName)==DatabaseColumnNameVariableTable.genericColumnNameNumber)
            {
                userInformationSqlObject.addSqlObject(columnName,userDataJsonObject.get(columnName));
                articleInformationSqlObject.addSqlObject(columnName,userDataJsonObject.get(columnName));
                classInformationSqlObject.addSqlObject(columnName,userDataJsonObject.get(columnName));
                userSpecialExperienceSqlObject.addSqlObject(columnName,userDataJsonObject.get(columnName));
                articleContentSqlObject.addSqlObject(columnName,userDataJsonObject.get(columnName));
            }
            else if(checkTableNumber(columnName)==DatabaseColumnNameVariableTable.usersInformationTableNumber)
            {
                userInformationSqlObject.addSqlObject(columnName,userDataJsonObject.get(columnName));
            }
            else if(checkTableNumber(columnName)==DatabaseColumnNameVariableTable.classInformationTableNumber)
            {
                classInformationSqlObject.addSqlObject(columnName,userDataJsonObject.get(columnName));
            }
            else if(checkTableNumber(columnName)==DatabaseColumnNameVariableTable.articlesInformationTableNumber)
            {
                articleInformationSqlObject.addSqlObject(columnName,userDataJsonObject.get(columnName));
            }
            else if(checkTableNumber(columnName)==DatabaseColumnNameVariableTable.usersSpecialExperienceTableNumber)
            {
                userSpecialExperienceSqlObject.addSqlObject(columnName,userDataJsonObject.get(columnName));
            }
            else if(checkTableNumber(columnName)==DatabaseColumnNameVariableTable.articlesContentTableNumber)
            {
                extractArticleContentColumnObject.setArticleContentColumnObject(columnName, userDataJsonObject);
                Object object = extractArticleContentColumnObject.getArticleContentColumnObject();
                articleContentSqlObject.addSqlObject(columnName, object);
            }
        }
        userData.setUserInformationSqlObject(userInformationSqlObject);
        userData.setClassInformationSqlObject(classInformationSqlObject);
        userData.setUserSpecialExperienceSqlObject(userSpecialExperienceSqlObject);
        userData.setArticleInformationSqlObject(articleInformationSqlObject);
        userData.setArticleContentSqlObject(articleContentSqlObject);
        return userData;
    }
    private int checkTableNumber(String columnName) {
        int tableNumber=-1;
        if(Arrays.asList(DatabaseColumnNameVariableTable.genericColumnNameList).contains(columnName))
        {
            tableNumber=DatabaseColumnNameVariableTable.genericColumnNameNumber;
        }
        else if(Arrays.asList(DatabaseColumnNameVariableTable.userInformationColumnNameList).contains(columnName))
        {
            tableNumber=DatabaseColumnNameVariableTable.usersInformationTableNumber;
        }
        else if(Arrays.asList(DatabaseColumnNameVariableTable.articlesInformationColumnNameList).contains(columnName))
        {
            tableNumber=DatabaseColumnNameVariableTable.articlesInformationTableNumber;
        }
        else if(Arrays.asList(DatabaseColumnNameVariableTable.classInformationColumnNameList).contains(columnName))
        {
            tableNumber=DatabaseColumnNameVariableTable.classInformationTableNumber;
        }
        else if(Arrays.asList(DatabaseColumnNameVariableTable.userSpecialInformationColumnNameList).contains(columnName))
        {
            tableNumber=DatabaseColumnNameVariableTable.usersSpecialExperienceTableNumber;
        }
        else if(Arrays.asList(DatabaseColumnNameVariableTable.articleContentColumnNameList).contains(columnName))
        {
            tableNumber=DatabaseColumnNameVariableTable.articlesContentTableNumber;
        }
        return tableNumber;
    }
}
