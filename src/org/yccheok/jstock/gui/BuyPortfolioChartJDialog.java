/*
 * JStock - Free Stock Market Software
 * Copyright (C) 2012 Yan Cheng CHEOK <yccheok@yahoo.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.yccheok.jstock.gui;

import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.yccheok.jstock.engine.Code;
import org.yccheok.jstock.engine.Symbol;
import org.yccheok.jstock.gui.treetable.BuyPortfolioTreeTableModelEx;
import org.yccheok.jstock.internationalization.GUIBundle;
import org.yccheok.jstock.portfolio.Dividend;
import org.yccheok.jstock.portfolio.DividendSummary;
import org.yccheok.jstock.portfolio.Portfolio;
import org.yccheok.jstock.portfolio.Transaction;
import org.yccheok.jstock.portfolio.TransactionSummary;

/**
 *
 * @author  yccheok
 */
public class BuyPortfolioChartJDialog extends javax.swing.JDialog {
    
    private static final String[] cNames = {
        GUIBundle.getString("BuyPortfolioTreeTableModel_NetGainValue"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_NetLossValue"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_GainValue"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_LossValue"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_NetGainPercentage"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_NetLossPercentage"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_GainPercentage"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_LossPercentage"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_Dividend"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_NetPurchaseValue"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_PurchaseValue"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_CurrentValue"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_GainPrice"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_LossPrice"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_PurchasePrice"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_CurrentPrice"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_Units"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_Broker"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_StampDuty"),
        GUIBundle.getString("BuyPortfolioTreeTableModel_ClearingFee"),
    };
    
    /** Creates new form BuyPortfolioChartJDialog */
    public BuyPortfolioChartJDialog(java.awt.Frame parent, boolean modal, BuyPortfolioTreeTableModelEx portfolioTreeTableModel, DividendSummary dividendSummary) {
        super(parent, GUIBundle.getString("BuyPortfolioChartJDialog_BuySummary"), modal);

        this.dividendSummary = dividendSummary;
        this.initCodeToTotalDividend(dividendSummary);

        initComponents();
        
        this.portfolioTreeTableModel = portfolioTreeTableModel;
        
        final JFreeChart freeChart = createChart(cNames[0]);

        org.yccheok.jstock.charting.Utils.applyChartTheme(freeChart);
        
        chartPanel = new ChartPanel(freeChart, true, true, true, true, true);
        
        getContentPane().add(chartPanel, java.awt.BorderLayout.CENTER);        
    }

    private void initCodeToTotalDividend(DividendSummary dividendSummary)
    {
        final int size = dividendSummary.size();
        for (int i = 0; i < size; i++) {
            Dividend dividend = dividendSummary.get(i);
            Code code = dividend.getStock().getCode();
            Double value = this.codeToTotalDividend.get(code);
            if (value != null) {
                double total = value + dividend.getAmount();
                this.codeToTotalDividend.put(code, total);
            }
            else {
                this.codeToTotalDividend.put(code, dividend.getAmount());
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        for(String cName : this.cNames) {
            this.jComboBox1.addItem(cName);
        }
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jComboBox1);

        jPanel1.add(jPanel2, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-750)/2, (screenSize.height-600)/2, 750, 600);
    }// </editor-fold>//GEN-END:initComponents

    private JFreeChart createChart(String name) {
        final Portfolio portfolio = (Portfolio)portfolioTreeTableModel.getRoot();
        final int count = portfolio.getChildCount();
        DefaultPieDataset data = new DefaultPieDataset();

        for (int i = 0; i < count; i++) {
            TransactionSummary transactionSummary = (TransactionSummary)portfolio.getChildAt(i);
            
            if(transactionSummary.getChildCount() <= 0) continue;
            
            Transaction transaction = (Transaction)transactionSummary.getChildAt(0);
            final Symbol symbol = transaction.getContract().getStock().getSymbol();
            final Code code =  transaction.getContract().getStock().getCode();

            /* Should use reflection technology. */
            if(name.equals(cNames[0])) {
                data.setValue(symbol.toString(), portfolioTreeTableModel.getNetGainLossValue(transactionSummary));
            }
            if(name.equals(cNames[1])) {
                data.setValue(symbol.toString(), -portfolioTreeTableModel.getNetGainLossValue(transactionSummary));
            }            
            else if(name.equals(cNames[2])) {
                data.setValue(symbol.toString(), portfolioTreeTableModel.getGainLossValue(transactionSummary));
            }
            else if(name.equals(cNames[3])) {
                data.setValue(symbol.toString(), -portfolioTreeTableModel.getGainLossValue(transactionSummary));
            }            
            else if(name.equals(cNames[4])) {
                data.setValue(symbol.toString(), portfolioTreeTableModel.getNetGainLossPercentage(transactionSummary));
            }
            else if(name.equals(cNames[5])) {
                data.setValue(symbol.toString(), -portfolioTreeTableModel.getNetGainLossPercentage(transactionSummary));
            }
            else if(name.equals(cNames[6])) {
                data.setValue(symbol.toString(), portfolioTreeTableModel.getGainLossPercentage(transactionSummary));
            }
            else if(name.equals(cNames[7])) {
                data.setValue(symbol.toString(), -portfolioTreeTableModel.getGainLossPercentage(transactionSummary));
            }            
            else if(name.equals(cNames[8])) {
                Double value = this.codeToTotalDividend.get(code);
                if (value != null) {
                    if (value.doubleValue() > 0.0) {
                        data.setValue(symbol.toString(), this.codeToTotalDividend.get(code));
                    }
                }
            }
            else if(name.equals(cNames[9])) {
                data.setValue(symbol.toString(), transactionSummary.getNetTotal());                
            }
            else if(name.equals(cNames[10])) {
                data.setValue(symbol.toString(), transactionSummary.getTotal());                
            }
            else if(name.equals(cNames[11])) {
                data.setValue(symbol.toString(), portfolioTreeTableModel.getCurrentValue(transactionSummary));                
            }
            else if(name.equals(cNames[12])) {
                data.setValue(symbol.toString(), portfolioTreeTableModel.getGainLossPrice(transactionSummary));                
            }
            else if(name.equals(cNames[13])) {
                data.setValue(symbol.toString(), -portfolioTreeTableModel.getGainLossPrice(transactionSummary));                
            }            
            else if(name.equals(cNames[14])) {
                data.setValue(symbol.toString(), portfolioTreeTableModel.getPurchasePrice(transactionSummary));                
            }
            else if(name.equals(cNames[15])) {
                data.setValue(symbol.toString(), portfolioTreeTableModel.getCurrentPrice(transactionSummary));                
            }
            else if(name.equals(cNames[16])) {
                data.setValue(symbol.toString(), transactionSummary.getQuantity());                
            }
            else if(name.equals(cNames[17])) {
                data.setValue(symbol.toString(), transactionSummary.getCalculatedBroker());                
            }
            else if(name.equals(cNames[18])) {
                data.setValue(symbol.toString(), transactionSummary.getCalculatedStampDuty());                
            }
            else if(name.equals(cNames[19])) {
                data.setValue(symbol.toString(), transactionSummary.getCalculatdClearingFee());                
            }
            
        }
        
        // create a chart...
        return ChartFactory.createPieChart(name, data, true, true, true);        
    }
    
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String selected = ((javax.swing.JComboBox)evt.getSource()).getSelectedItem().toString();
        
        final JFreeChart freeChart = this.createChart(selected);
        org.yccheok.jstock.charting.Utils.applyChartTheme(freeChart);
        chartPanel.setChart(freeChart);
    }//GEN-LAST:event_jComboBox1ActionPerformed
    
    private final BuyPortfolioTreeTableModelEx portfolioTreeTableModel;
    private final ChartPanel chartPanel;
    private final DividendSummary dividendSummary;
    private final Map<Code, Double> codeToTotalDividend = new HashMap<Code, Double>();
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
    
}
